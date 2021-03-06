/*
 * Copyright 2003 - 2016 The eFaps Team
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
 */


package org.efaps.esjp.ui.html.dojo.charting;

import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 */
@EFapsUUID("d9f2f6ab-cdc2-4c26-bd42-6d402d9687ab")
@EFapsApplication("eFaps-WebApp")
public enum Orientation
{

    /** The vertical chart legend. */
    VERTICAL_CHART_LEGEND,

    /** The vertical legend chart. */
    VERTICAL_LEGEND_CHART,

    /** The horizontal chart legend. */
    HORIZONTAL_CHART_LEGEND,

    /** The horizontal legend chart. */
    HORIZONTAL_LEGEND_CHART,

    /** The chart only. */
    CHART_ONLY
}
