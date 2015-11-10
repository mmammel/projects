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

import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.13 $
 */
public class OpenAction extends StandardAction {
    private final static String ACTION_COMMAND_KEY_OPEN = "open-command";
    private final static String NAME_OPEN = "Open";
    private final static String SMALL_ICON_OPEN = "/com/sshtools/common/ui/fileopen.png";
    private final static String LARGE_ICON_OPEN = "";
    private final static String SHORT_DESCRIPTION_OPEN = "Open an existing connection";
    private final static String LONG_DESCRIPTION_OPEN = "Opens an existing connection from file";
    private final static int MNEMONIC_KEY_OPEN = 'O';

    /**
* Creates a new OpenAction object.
*/
    public OpenAction() {
        putValue(Action.NAME, NAME_OPEN);
        putValue(Action.SMALL_ICON, getIcon(SMALL_ICON_OPEN));
        putValue(LARGE_ICON, getIcon(LARGE_ICON_OPEN));
        putValue(Action.SHORT_DESCRIPTION, SHORT_DESCRIPTION_OPEN);
        putValue(Action.LONG_DESCRIPTION, LONG_DESCRIPTION_OPEN);
        putValue(Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.ALT_MASK));
        putValue(Action.MNEMONIC_KEY, new Integer(MNEMONIC_KEY_OPEN));
        putValue(Action.ACTION_COMMAND_KEY, ACTION_COMMAND_KEY_OPEN);
        putValue(StandardAction.ON_MENUBAR, new Boolean(true));
        putValue(StandardAction.MENU_NAME, "File");
        putValue(StandardAction.MENU_ITEM_GROUP, new Integer(0));
        putValue(StandardAction.MENU_ITEM_WEIGHT, new Integer(5));
        putValue(StandardAction.ON_TOOLBAR, new Boolean(true));
        putValue(StandardAction.TOOLBAR_GROUP, new Integer(0));
        putValue(StandardAction.TOOLBAR_WEIGHT, new Integer(5));
    }
}
