package com.angryBirds.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;

public class musicControl {
    private static musicControl instance;
    private Music currentMusic;
    private float targetVolume = 1f;
    private float currentVolume = 1f;
    private boolean isMuted = false;
    private float fadeTime = 2f; // Default fade duration in seconds
    private Timer.Task fadeTask;

    public static musicControl getInstance() {
        if (instance == null) {
            instance = new musicControl();
        }
        return instance;
    }

    public void loadMusic(String musicPath) {
        if (currentMusic != null) {
            currentMusic.dispose();
        }
        try {
            currentMusic = Gdx.audio.newMusic(Gdx.files.internal(musicPath));
            currentMusic.setLooping(true);
            currentMusic.setVolume(currentVolume);
        } catch (Exception e) {
            Gdx.app.error("AudioController", "Error loading music: " + musicPath, e);
        }
    }

    public void playMusic() {
        if (currentMusic != null && !isMuted) {
            currentMusic.play();
            currentMusic.setVolume(currentVolume);
        }
    }

    public void pauseMusic() {
        if (currentMusic != null) {
            currentMusic.pause();
        }
    }

    public void fadeIn() {
        fadeIn(fadeTime);
    }

    public void fadeIn(float duration) {
        if (currentMusic == null || isMuted) return;

        stopFadeTask();
        currentMusic.setVolume(0f);
        currentMusic.play();

        fadeTask = Timer.schedule(new Timer.Task() {
            float elapsed = 0f;

            @Override
            public void run() {
                elapsed += Gdx.graphics.getDeltaTime();
                float progress = Math.min(elapsed / duration, 1f);
                currentVolume = progress * targetVolume;
                currentMusic.setVolume(currentVolume*0.2f);

                if (progress >= 1f) {
                    this.cancel();
                }
            }
        }, 0f, 1/60f); // Update 60 times per second
    }

    public void fadeOut() {
        fadeOut(fadeTime);
    }

    public void fadeOut(float duration) {
        if (currentMusic == null) return;

        stopFadeTask();
        final float startVolume = currentVolume;

        fadeTask = Timer.schedule(new Timer.Task() {
            float elapsed = 0f;

            @Override
            public void run() {
                elapsed += Gdx.graphics.getDeltaTime();
                float progress = Math.min(elapsed / duration, 1f);
                currentVolume = startVolume * (1f - progress);
                currentMusic.setVolume(currentVolume*0.2f);

                if (progress >= 1f) {
                    currentMusic.pause();
                    this.cancel();
                }
            }
        }, 0f, 1/60f);
    }

    public void crossFade(String newMusicPath, float duration) {
        if (currentMusic != null && currentMusic.isPlaying()) {
            // Store the previous music reference
            final Music previousMusic = currentMusic;
            final float prevVolume = currentVolume;

            // Reset current music reference but don't dispose the previous one yet
            currentMusic = null;

            try {
                // Load new music
                currentMusic = Gdx.audio.newMusic(Gdx.files.internal(newMusicPath));
                currentMusic.setLooping(true);
                currentMusic.setVolume(0f); // Start at zero volume
                currentMusic.play();

                // Create crossfade effect
                Timer.schedule(new Timer.Task() {
                    float elapsed = 0f;
                    boolean isRunning = true;

                    @Override
                    public void run() {
                        if (!isRunning) return;

                        elapsed += Gdx.graphics.getDeltaTime();
                        float progress = Math.min(elapsed / duration, 1f);

                        // Fade out old music
                        if (previousMusic != null && previousMusic.isPlaying()) {
                            previousMusic.setVolume(prevVolume * (1f - progress));
                        }

                        // Fade in new music
                        if (currentMusic != null && currentMusic.isPlaying()) {
                            currentMusic.setVolume(targetVolume * progress*0.2f);
                        }

                        // When fade is complete
                        if (progress >= 1f) {
                            isRunning = false;
                            if (previousMusic != null) {
                                previousMusic.stop();
                                previousMusic.dispose();
                            }
                            this.cancel();
                        }
                    }
                }, 0f, 1/60f);

            } catch (Exception e) {
                Gdx.app.error("musicControl", "Error during crossfade to: " + newMusicPath, e);
                // Cleanup in case of error
                if (previousMusic != null) {
                    previousMusic.dispose();
                }
                if (currentMusic != null) {
                    currentMusic.dispose();
                }
            }
        } else {
            // If no music is currently playing, simply load and fade in the new music
            loadMusic(newMusicPath);
            fadeIn(duration);
        }
    }

    private void stopFadeTask() {
        if (fadeTask != null) {
            fadeTask.cancel();
            fadeTask = null;
        }
    }

    public void setVolume(float volume) {
        this.targetVolume = Math.max(0f, Math.min(1f, volume));
        if (currentMusic != null) {
            currentMusic.setVolume(targetVolume);
        }
        currentVolume = targetVolume;
    }

    public void toggleMute() {
        isMuted = !isMuted;
        if (currentMusic != null) {
            if (isMuted) {
                currentMusic.setVolume(0f);
            } else {
                currentMusic.setVolume(currentVolume);
            }
        }
    }

    public boolean isPlaying() {
        return currentMusic != null && currentMusic.isPlaying();
    }

    public void dispose() {
        stopFadeTask();
        if (currentMusic != null) {
            currentMusic.dispose();
        }
    }
}
