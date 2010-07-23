/*
 * Copyright 2003 - 2010 The eFaps Team
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

package org.efaps.esjp.ui.print;

import java.awt.Color;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperPrint;

import org.efaps.admin.datamodel.Attribute;
import org.efaps.admin.datamodel.attributetype.BooleanType;
import org.efaps.admin.datamodel.attributetype.DateTimeType;
import org.efaps.admin.datamodel.attributetype.DecimalType;
import org.efaps.admin.datamodel.attributetype.IntegerType;
import org.efaps.admin.datamodel.attributetype.LongType;
import org.efaps.admin.datamodel.attributetype.RateType;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Return;
import org.efaps.admin.event.Parameter.ParameterValues;
import org.efaps.admin.event.Return.ReturnValues;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.admin.ui.AbstractUserInterfaceObject.TargetMode;
import org.efaps.admin.ui.field.Field;
import org.efaps.db.Context;
import org.efaps.ui.wicket.models.cell.UITableCell;
import org.efaps.ui.wicket.models.objects.UIAbstractPageObject;
import org.efaps.ui.wicket.models.objects.UIRow;
import org.efaps.ui.wicket.models.objects.UITable;
import org.efaps.ui.wicket.models.objects.UITableHeader;
import org.efaps.util.EFapsException;
import org.joda.time.DateTime;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.ListLayoutManager;
import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.constants.Stretching;
import ar.com.fdvs.dj.domain.constants.Transparency;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("99ce434b-4177-4e65-99d1-0195434f628d")
@EFapsRevision("$Rev$")
public abstract class Table_Base
    extends UserInterface
{

    /**
     * @param _parameter Parameter
     * @return return PDF with Table
     * @throws EFapsException on error
     */
    @Override
    public Return execute(final Parameter _parameter)
        throws EFapsException
    {
        final Return ret = new Return();
        final Map<?, ?> properties = (Map<?, ?>) _parameter.get(ParameterValues.PROPERTIES);
        final Object object = Context.getThreadContext().getSessionAttribute(UserInterface_Base.UIOBJECT_CACHEKEY);
        if (object instanceof UIAbstractPageObject) {

            final UIAbstractPageObject page = (UIAbstractPageObject) object;

            if (page instanceof UITable) {
                String mime = (String) properties.get("Mime");
                if (mime == null) {
                    mime = _parameter.getParameterValue("mime");
                }
                final boolean print = "pdf".equalsIgnoreCase(mime);
                if (!print) {
                    page.resetModel();
                    page.setMode(TargetMode.PRINT);
                    page.execute();
                }

                setFileName(page.getTitle());

                final Style detailStyle = new Style();

                final Style headerStyle = new Style();
                final Style titleStyle = new Style();
                titleStyle.setFont(Font.VERDANA_BIG_BOLD);
                final Style subtitleStyle = new Style();

                final Style columnStyle = new Style();
                columnStyle.setBorder(Border.PEN_1_POINT);

                final Style columnHeaderStyle = new Style();
                columnHeaderStyle.setBackgroundColor(Color.gray);
                columnHeaderStyle.setBorder(Border.PEN_1_POINT);
                columnHeaderStyle.setTransparency(Transparency.OPAQUE);
                columnHeaderStyle.setTextColor(Color.white);
                columnHeaderStyle.setStreching(Stretching.NO_STRETCH);

                final DynamicReportBuilder drb = new DynamicReportBuilder()
                    .setTitle(page.getTitle())
                    .setUseFullPageWidth(true);

                if (print) {
                    drb.setDetailHeight(15)
                        .setHeaderHeight(15)
                        .setDefaultStyles(titleStyle, subtitleStyle, headerStyle, detailStyle)
                        .setColumnsPerPage(1)
                        .setPageSizeAndOrientation(Page.Page_A4_Landscape())
                        .setMargins(20, 20, 20, 20); // (top, bottom, left and right)
                } else {
                    drb.setPrintColumnNames(true)
                        .setIgnorePagination(true)
                        .setMargins(0, 0, 0, 0)
                        .setReportName(page.getTitle());
                }

                try {
                    final int widthWeight = ((UITable) page).getWidthWeight();
                    final String[] columns = _parameter.getParameterValues("columns");
                    final Set<String> selCols = new HashSet<String>();
                    for (final String col : columns) {
                        selCols.add(col);
                    }
                    for (final UITableHeader header : ((UITable) page).getHeaders()) {
                        final boolean add;
                        if (print) {
                            add = selCols.contains(header.getFieldName());
                        } else {
                            final Field field = Field.get(header.getFieldId());
                            add = (field.isNoneDisplay(TargetMode.VIEW) && !field.isNoneDisplay(TargetMode.PRINT))
                                || selCols.contains(header.getFieldName());
                            if (add && !selCols.contains(header.getFieldName())) {
                                selCols.add(header.getFieldName());
                            }
                        }
                        if (add) {
                            final BigDecimal width = new BigDecimal(header.getWidth()).setScale(2)
                                .divide( new BigDecimal(widthWeight), BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(555));

                            final ColumnBuilder cbldr = ColumnBuilder.getInstance();
                            String clazzname = String.class.getName();
                            final Attribute attr = header.getAttribute();
                            if (attr != null && !print) {
                                if (attr.getAttributeType().getDbAttrType() instanceof LongType) {
                                    clazzname = Long.class.getName();
                                } else if (attr.getAttributeType().getDbAttrType() instanceof LongType) {
                                    clazzname = Long.class.getName();
                                } else if (attr.getAttributeType().getDbAttrType() instanceof DecimalType) {
                                    clazzname = BigDecimal.class.getName();
                                } else if (attr.getAttributeType().getDbAttrType() instanceof IntegerType) {
                                    clazzname = Integer.class.getName();
                                } else if (attr.getAttributeType().getDbAttrType() instanceof BooleanType) {
                                    // TODO this should be boolean.
                                    clazzname = String.class.getName();
                                } else if (attr.getAttributeType().getDbAttrType() instanceof DateTimeType) {
                                    clazzname = Date.class.getName();
                                } else if (attr.getAttributeType().getDbAttrType() instanceof RateType) {
                                    clazzname = BigDecimal.class.getName();
                                }
                            }
                            cbldr.setColumnProperty(header.getFieldName(), clazzname)
                                .setTitle(header.getLabel());
                            if (print) {
                                cbldr.setStyle(columnStyle)
                                    .setHeaderStyle(columnHeaderStyle)
                                    .setWidth(header.isFixedWidth() ? header.getWidth() : width.intValue());
                            }
                            drb.addColumn(cbldr.build());
                        }
                    }

                    final List<Map <String, Object>> values = new ArrayList<Map <String, Object>>();
                    for (final UIRow row : ((UITable) page).getValues()) {
                        final Map<String, Object> map = new HashMap<String, Object>();
                        for (final UITableCell cell : row.getValues()) {
                            if (selCols.contains(cell.getName())) {
                                Object value = print ?  cell.getCellTitle() : (cell.getCompareValue() != null
                                                ? cell.getCompareValue() : cell.getCellTitle());
                                if (value instanceof DateTime) {
                                    value = ((DateTime) value).toDate();
                                }
                                map.put(cell.getName(), value);
                            }
                        }
                        values.add(map);
                    }

                    if (print) {
                        drb.addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_FOOTER,
                                        AutoText.ALIGMENT_RIGHT, 200, 40);
                        drb.addAutoText(AutoText.AUTOTEXT_CREATED_ON, AutoText.POSITION_HEADER, AutoText.ALIGMENT_RIGHT,
                                    AutoText.PATTERN_DATE_DATE_TIME);
                    }
                    drb.setReportLocale(Context.getThreadContext().getLocale());
                    final DynamicReport dr = drb.build(); // Finally build the
                    // report!
                    final JRDataSource ds = new TableSource(values);
                    final JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr,
                                    print ? new  ClassicLayoutManager() : new ListLayoutManager(), ds);
                    ret.put(ReturnValues.VALUES, super.getFile(jp, mime));
                    ret.put(ReturnValues.TRUE, true);
                } catch (final ColumnBuilderException e) {
                   throw new EFapsException(Table_Base.class, "ColumnBuilderException", e);
                } catch (final JRException e) {
                    throw new EFapsException(Table_Base.class, "JRException", e);
                } catch (final IOException e) {
                    throw new EFapsException(Table_Base.class, "IOException", e);
                }
            } else {
                // ??
            }
        }
        return ret;
    }

    /**
     * Source for a Table.
     */
    private class TableSource
        implements JRDataSource
    {
        /**
         * Values for the rows.
         */
        private final Iterator<Map<String, Object>> values;

        /**
         * Value for the current row.
         */
        private Map<String, Object> current;

        /**
         * @param _values values for the rows
         */
        public TableSource(final List<Map<String, Object>> _values)
        {
            this.values = _values.iterator();
        }

        /**
         * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
         * @param _field Field the value is returned for
         * @return value for the field
         * @throws JRException on error
         */
        public Object getFieldValue(final JRField _field)
            throws JRException
        {
            return this.current.get(_field.getName());
        }

        /**
         * @see net.sf.jasperreports.engine.JRDataSource#next()
         * @return true if next
         * @throws JRException on error
         */
        public boolean next()
            throws JRException
        {
            final boolean ret = this.values.hasNext();
            if (ret) {
                this.current = this.values.next();
            }
            return ret;
        }
    }
}
