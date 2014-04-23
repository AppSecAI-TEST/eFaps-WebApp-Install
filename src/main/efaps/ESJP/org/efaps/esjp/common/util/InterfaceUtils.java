/*
 * Copyright 2003 - 2014 The eFaps Team
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


package org.efaps.esjp.common.util;

import java.util.Map;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class InterfaceUtils
    extends InterfaceUtils_Base
{

    /**
     * @param _map
     * @param _js
     */
    public static void appendScript4FieldUpdate(final Map<String, Object> _map,
                                                final CharSequence _script)
    {
        InterfaceUtils_Base.appendScript4FieldUpdate(_map, _script);
    }

}
