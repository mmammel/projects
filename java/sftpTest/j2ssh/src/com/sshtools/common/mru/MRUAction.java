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
package com.sshtools.common.mru;

import com.sshtools.common.ui.EmptyIcon;
import com.sshtools.common.ui.MenuAction;
import com.sshtools.common.ui.StandardAction;

import javax.swing.Action;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.14 $
 */
public abstract class MRUAction extends MenuAction {
    /**
* Creates a new MRUAction object.
*
* @param model
*/
    public MRUAction(MRUListModel model) {
        putValue(Action.NAME, "Recent");
        putValue(Action.SMALL_ICON, new EmptyIcon(16, 16));
        putValue(Action.SHORT_DESCRIPTION, "Recent connections");
        putValue(Action.LONG_DESCRIPTION, "Recent connection files");
        putValue(Action.MNEMONIC_KEY, new Integer('r'));
        putValue(Action.ACTION_COMMAND_KEY, "recent");
        putValue(StandardAction.ON_MENUBAR, new Boolean(true));
        putValue(StandardAction.MENU_NAME, "File");
        putValue(StandardAction.MENU_ITEM_GROUP, new Integer(0));
        putValue(StandardAction.MENU_ITEM_WEIGHT, new Integer(99));

        MRUMenu menu = new MRUMenu(this, model);
        menu.addActionListener(this);
        putValue(MenuAction.MENU, new MRUMenu(this, model));
    }
}
