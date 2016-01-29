/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.backends.jogamp.audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.openal.*;
import com.badlogic.gdx.audio.Sound;

/** @author Nathan Sweet */
public class OpenALSound implements Sound {
	private int bufferID = -1;
	private IntBuffer ib = Buffers.newDirectIntBuffer(1);
	private final OpenALAudio audio;
	private float duration;

	public OpenALSound (OpenALAudio audio) {
		this.audio = audio;
	}

	void setup (byte[] pcm, int channels, int sampleRate) {
		int bytes = pcm.length - (pcm.length % (channels > 1 ? 4 : 2));
		int samples = bytes / (2 * channels);
		duration = samples / (float)sampleRate;

		ByteBuffer buffer = ByteBuffer.allocateDirect(bytes);
		buffer.order(ByteOrder.nativeOrder());
		buffer.put(pcm, 0, bytes);
		buffer.flip();

		if (bufferID == -1) {
			audio.getAL().alGenBuffers(ib.limit(), ib);
			bufferID = ib.get(0);
			audio.getAL().alBufferData(bufferID, channels > 1 ? ALConstants.AL_FORMAT_STEREO16 : ALConstants.AL_FORMAT_MONO16, buffer.asShortBuffer(), buffer.remaining(), sampleRate);
		}
	}

	public long play () {
		return play(1);
	}

	public long play (float volume) {
		if (audio.noDevice) return 0;
		int sourceID = audio.obtainSource(false);
		if (sourceID == -1) {
			// Attempt to recover by stopping the least recently played sound
			audio.retain(this, true);
			sourceID = audio.obtainSource(false);
		} else audio.retain(this, false);
		// In case it still didn't work
		if (sourceID == -1) return -1;
		long soundId = audio.getSoundId(sourceID);
		audio.getAL().alSourcei(sourceID, ALConstants.AL_BUFFER, bufferID);
		audio.getAL().alSourcei(sourceID, ALConstants.AL_LOOPING, ALConstants.AL_FALSE);
		audio.getAL().alSourcef(sourceID, ALConstants.AL_GAIN, volume);
		audio.getAL().alSourcePlay(sourceID);
		return soundId;
	}

	public long loop () {
		return loop(1);
	}

	@Override
	public long loop (float volume) {
		if (audio.noDevice) return 0;
		int sourceID = audio.obtainSource(false);
		if (sourceID == -1) return -1;
		long soundId = audio.getSoundId(sourceID);
		audio.getAL().alSourcei(sourceID, ALConstants.AL_BUFFER, bufferID);
		audio.getAL().alSourcei(sourceID, ALConstants.AL_LOOPING, ALConstants.AL_TRUE);
		audio.getAL().alSourcef(sourceID, ALConstants.AL_GAIN, volume);
		audio.getAL().alSourcePlay(sourceID);
		return soundId;
	}

	public void stop () {
		if (audio.noDevice) return;
		audio.stopSourcesWithBuffer(bufferID);
	}

	public void dispose () {
		if (audio.noDevice) return;
		if (bufferID == -1) return;
		audio.freeBuffer(bufferID);
		ib.put(0, bufferID);
		audio.getAL().alDeleteBuffers(ib.limit(), ib);
		bufferID = -1;
	}

	@Override
	public void stop (long soundId) {
		if (audio.noDevice) return;
		audio.stopSound(soundId);
	}

	@Override
	public void pause () {
		if (audio.noDevice) return;
		audio.pauseSourcesWithBuffer(bufferID);
	}

	@Override
	public void pause (long soundId) {
		if (audio.noDevice) return;
		audio.pauseSound(soundId);
	}

	@Override
	public void resume () {
		if (audio.noDevice) return;
		audio.resumeSourcesWithBuffer(bufferID);
	}

	@Override
	public void resume (long soundId) {
		if (audio.noDevice) return;
		audio.resumeSound(soundId);
	}

	@Override
	public void setPitch (long soundId, float pitch) {
		if (audio.noDevice) return;
		audio.setSoundPitch(soundId, pitch);
	}

	@Override
	public void setVolume (long soundId, float volume) {
		if (audio.noDevice) return;
		audio.setSoundGain(soundId, volume);
	}

	@Override
	public void setLooping (long soundId, boolean looping) {
		if (audio.noDevice) return;
		audio.setSoundLooping(soundId, looping);
	}

	@Override
	public void setPan (long soundId, float pan, float volume) {
		if (audio.noDevice) return;
		audio.setSoundPan(soundId, pan, volume);
	}

	@Override
	public long play (float volume, float pitch, float pan) {
		long id = play();
		setPitch(id, pitch);
		setPan(id, pan, volume);
		return id;
	}

	@Override
	public long loop (float volume, float pitch, float pan) {
		long id = loop();
		setPitch(id, pitch);
		setPan(id, pan, volume);
		return id;
	}

	/** Returns the length of the sound in seconds. */
	public float duration () {
		return duration;
	}
}
