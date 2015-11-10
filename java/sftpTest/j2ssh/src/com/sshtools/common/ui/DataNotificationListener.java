/*
 *  SSHTools - Java SSH2 API
 *
 *  Copyright (C) 2002-2003 Lee David Painter and Contributors.
 *
 *  Contributions made by:
 *
 *  Brett Smith
 *  Richard Pernavas
 *  Erwin Bolwidt
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.sshtools.common.ui;

import com.sshtools.j2ssh.connection.Channel;
import com.sshtools.j2ssh.connection.ChannelEventAdapter;
import com.sshtools.j2ssh.connection.ChannelEventListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;


public class DataNotificationListener extends ChannelEventAdapter {
    Timer timerReceiving;
    Timer timerSending;
    StatusBar bar;

    public DataNotificationListener(StatusBar bar) {
        this.bar = bar;
        timerReceiving = new Timer(500, new ReceivingActionListener());
        timerReceiving.setRepeats(false);
        timerSending = new Timer(500, new SendingActionListener());
        timerSending.setRepeats(false);
    }

    public void actionPerformed(ActionEvent evt) {
        bar.setReceiving(false);
    }

    public void onDataReceived(Channel channel, byte[] data) {
        if (!timerReceiving.isRunning()) {
            bar.setReceiving(true);
            timerReceiving.start();
        }
    }

    public void onDataSent(Channel channel, byte[] data) {
        if (!timerSending.isRunning()) {
            bar.setSending(true);
            timerSending.start();
        }
    }

    class SendingActionListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            bar.setSending(false);
        }
    }

    class ReceivingActionListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            bar.setReceiving(false);
        }
    }
}
