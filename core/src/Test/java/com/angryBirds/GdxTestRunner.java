package com.angryBirds;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import static org.mockito.Mockito.mock;

public class GdxTestRunner implements BeforeAllCallback {
    private static boolean initialized = false;
    private static Object lock = new Object();
    private static HeadlessApplication app;

    @Override
    public void beforeAll(ExtensionContext context) {
        synchronized (lock) {
            if (!initialized) {
                HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
                app = new HeadlessApplication(new ApplicationAdapter() {
                    @Override
                    public void create() {
                        Gdx.gl = mock(GL20.class);
                        Gdx.gl20 = mock(GL20.class);
                        initialized = true;
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                    }
                }, config);

                while (!initialized) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException("Failed to initialize LibGDX context", e);
                    }
                }
            }
        }
    }

    public static void cleanUp() {
        if (app != null) {
            app.exit();
            app = null;
            initialized = false;
        }
    }
}