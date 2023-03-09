package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, rightPressed, leftPressed, spacePressed, mutePressed;

    @Override
    public void keyPressed(KeyEvent e) {

        int keycode = e.getKeyCode();

        if (keycode == KeyEvent.VK_W)
        {
            upPressed = true;
        }

        if (keycode == KeyEvent.VK_S)
        {
            downPressed = true;
        }

        if (keycode == KeyEvent.VK_D)
        {
            rightPressed = true;
        }

        if (keycode == KeyEvent.VK_A)
        {
            leftPressed = true;
        }

        if (keycode == KeyEvent.VK_SPACE)
        {
            spacePressed = true;
        }

        if (keycode == KeyEvent.VK_M)
        {
            mutePressed = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        int keycode = e.getKeyCode();

        if (keycode == KeyEvent.VK_W)
        {
            upPressed = false;
        }

        if (keycode == KeyEvent.VK_S)
        {
            downPressed = false;
        }

        if (keycode == KeyEvent.VK_D)
        {
            rightPressed = false;
        }

        if (keycode == KeyEvent.VK_A)
        {
            leftPressed = false;
        }

        if (keycode == KeyEvent.VK_SPACE)
        {
            spacePressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
