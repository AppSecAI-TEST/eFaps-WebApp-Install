/*
 * Copyright 2003 - 2013 The eFaps Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision:        $Rev$
 * Last Changed:    $Date$
 * Last Changed By: $Author$
 */

package org.efaps.esjp.ui.dashboard;

import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.ui.wicket.models.IEsjpSnipplet;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("a1f59464-0354-47c4-ac92-0f393d5bfa8a")
@EFapsRevision("$Rev$")
public class StatusPanel_Base
    implements IEsjpSnipplet
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CharSequence getHtmlSnipplet()
        throws EFapsException
    {
        return "Hello Welt";
    }

    @Override
    public boolean isVisible()
        throws EFapsException
    {
        return true;
    }
}
