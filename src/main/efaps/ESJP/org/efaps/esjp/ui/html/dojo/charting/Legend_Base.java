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


package org.efaps.esjp.ui.html.dojo.charting;

import org.apache.commons.lang3.RandomStringUtils;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("f155aac0-fa27-4cf8-bfd2-d02e4dd1c128")
@EFapsRevision("$Rev$")
public abstract class Legend_Base
{
    private boolean vertical = false;

    private String nodeId;

    private  int horizontalCount = 5;

    public CharSequence getScriptPart(final String _chart)
    {
        final StringBuilder ret = new StringBuilder();
        ret.append("new Legend({\n")
            .append(" chart: ").append(_chart);

        if (isVertical()) {
            ret.append(",\n horizontal: false");
        } else if (getHorizontalCount() > 0) {
            ret.append(",\n horizontal: ").append(getHorizontalCount());
        }

        ret.append(" },\n\"").append(getNodeId()).append("\");\n")
            .append(" });");
        return ret;
    }

    /**
     * Getter method for the instance variable {@link #legendNodeId}.
     *
     * @return value of instance variable {@link #legendNodeId}
     */
    public String getNodeId()
    {
        if (this.nodeId == null) {
            this.nodeId = RandomStringUtils.randomAlphabetic(8);
        }
        return this.nodeId;
    }

    /**
     * Setter method for instance variable {@link #legendNodeId}.
     *
     * @param _legendNodeId value for instance variable {@link #legendNodeId}
     */
    public void setNodeId(final String _legendNodeId)
    {
        this.nodeId = _legendNodeId;
    }

    /**
     * Getter method for the instance variable {@link #horizontal}.
     *
     * @return value of instance variable {@link #horizontal}
     */
    public boolean isVertical()
    {
        return this.vertical;
    }

    /**
     * Setter method for instance variable {@link #horizontal}.
     *
     * @param _horizontal value for instance variable {@link #horizontal}
     */
    public void setVertical(final boolean _vertical)
    {
        this.vertical = _vertical;
    }

    /**
     * Getter method for the instance variable {@link #horizontalCount}.
     *
     * @return value of instance variable {@link #horizontalCount}
     */
    public int getHorizontalCount()
    {
        return this.horizontalCount;
    }

    /**
     * Setter method for instance variable {@link #horizontalCount}.
     *
     * @param _horizontalCount value for instance variable {@link #horizontalCount}
     */
    public void setHorizontalCount(final int _horizontalCount)
    {
        this.horizontalCount = _horizontalCount;
    }
}