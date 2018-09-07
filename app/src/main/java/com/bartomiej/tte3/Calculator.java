package com.bartomiej.tte3;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculator {

    static int dataUnitFactor = 1;
    static int resultsUnitFactor = 1;

    private static Double round(Double value, int places) {
        if(places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);

        bd = bd.setScale(places, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }

    public static void updateUnits(Activity activity, Double updateFactor) {
        TextView a = (TextView) activity.findViewById(R.id.a);
        TextView at = (TextView) activity.findViewById(R.id.at);
        TextView e = (TextView) activity.findViewById(R.id.e);
        TextView iy = (TextView) activity.findViewById(R.id.iy);
        TextView w = (TextView) activity.findViewById(R.id.w);
        Double tmp;

        tmp = Double.parseDouble(a.getText().toString())*updateFactor*updateFactor;
        a.setText(round(tmp,2).toString());

        tmp = Double.parseDouble(at.getText().toString())*updateFactor*updateFactor;
        at.setText(round(tmp,2).toString());

        tmp = Double.parseDouble(e.getText().toString())*updateFactor;
        e.setText(round(tmp,2).toString());

        tmp = Double.parseDouble(iy.getText().toString())*updateFactor*updateFactor*updateFactor*updateFactor;
        iy.setText(round(tmp,2).toString());

        tmp = Double.parseDouble(w.getText().toString())*updateFactor*updateFactor*updateFactor;
        w.setText(round(tmp,2).toString());
    }

    private static void checkDataUnits(int unitFactor) {

    }

    private static int setDataUnits(String a) {
        int tmp = 0;
        switch (a) {
            case "mm":
                tmp= 1;
                break;
            case "cm":
                tmp = 10;
                break;
            case "dm":
                tmp = 100;
                break;
            case "m":
                tmp = 1000;
                break;
        }

        return tmp;
    }


    public static void calculate(Activity activity) {
        Spinner data = (Spinner) activity.findViewById(R.id.s_data);
        Spinner results = (Spinner) activity.findViewById(R.id.s_results);
        Switch cienkoscienny = (Switch) activity.findViewById(R.id.cienkoscienny);
        Switch plaskownik = (Switch) activity.findViewById(R.id.plaskownik);
        EditText pas_wsp_szer = (EditText) activity.findViewById(R.id.pas_wsp_szer);
        EditText pas_wsp_gr = (EditText) activity.findViewById(R.id.pas_wsp_gr);
        EditText srod_szer = (EditText) activity.findViewById(R.id.srod_szer);
        EditText srod_gr = (EditText) activity.findViewById(R.id.srod_gr);
        EditText moc_szer = (EditText) activity.findViewById(R.id.moc_szer);
        EditText moc_gr = (EditText) activity.findViewById(R.id.moc_gr);
        TextView a = (TextView) activity.findViewById(R.id.a);
        TextView at = (TextView) activity.findViewById(R.id.at);
        TextView e = (TextView) activity.findViewById(R.id.e);
        TextView iy = (TextView) activity.findViewById(R.id.iy);
        TextView w = (TextView) activity.findViewById(R.id.w);
        Double da, dat, de, dsy, diy, dw;
        String mark = srod_szer.getText().toString()+"x"+srod_gr.getText().toString();

        Double pws = pas_wsp_szer.getText().toString().matches("") ? 0 : Double.parseDouble(pas_wsp_szer.getText().toString());
        Double pwg = pas_wsp_gr.getText().toString().matches("") ? 0 : Double.parseDouble(pas_wsp_gr.getText().toString());
        Double ss = srod_szer.getText().toString().matches("") ? 0 : Double.parseDouble(srod_szer.getText().toString());
        Double sg = srod_gr.getText().toString().matches("") ? 0 : Double.parseDouble(srod_gr.getText().toString());
        Double ms = moc_szer.getText().toString().matches("") ? 0 : Double.parseDouble(moc_szer.getText().toString());
        Double mg = moc_gr.getText().toString().matches("") ? 0 : Double.parseDouble(moc_gr.getText().toString());

        checkDataUnits(dataUnitFactor);

        dataUnitFactor = setDataUnits(data.getSelectedItem().toString());
        resultsUnitFactor = setDataUnits(results.getSelectedItem().toString());

        /*switch (data.getSelectedItem().toString()) {
            case "mm":
                dataUnitFactor = 1;
                break;
            case "cm":
                dataUnitFactor = 10;
                break;
            case "dm":
                dataUnitFactor = 100;
                break;
            case "m":
                dataUnitFactor = 1000;
                break;
        }

        switch (results.getSelectedItem().toString()) {
            case "mm":
                resultsUnitFactor = 1;
                break;
            case "cm":
                resultsUnitFactor = 10;
                break;
            case "dm":
                resultsUnitFactor = 100;
                break;
            case "m":
                resultsUnitFactor = 1000;
                break;
        }*/

        if(cienkoscienny.isChecked() && plaskownik.isChecked()) {
            da = pws*pwg+ss*sg+ms*mg;
            a.setText(round(da, 2).toString());

            dat = sg*ss;
            at.setText(round(dat, 2).toString());

            dsy = sg*ss*ss/2+ms*mg*ss;
            de = da == 0 ? 0 : dsy/da;
            e.setText(round((de+pwg),2).toString());

            diy = sg*ss*ss*ss/3+ms*mg*ss*ss-da*de*de;
            BigDecimal bd = new BigDecimal(diy);
            iy.setText(bd.setScale(2, RoundingMode.HALF_UP).toString());

            dw = (ss-de) == 0 ? 0 : diy/(ss-de);;
            w.setText(round(dw, 2).toString());
        } else if(!cienkoscienny.isChecked() && plaskownik.isChecked()) {
            da = pws*pwg+ss*sg+ms*mg;
            a.setText(round(da, 2).toString());

            dat = sg*ss;
            at.setText(round(dat, 2).toString());

            dsy = ((sg*ss*ss)/2)+ms*mg*(ss+mg/2)-((pws*pwg*pwg)/2);
            de = da == 0 ? 0 : dsy/da;
            e.setText(round((de+pwg),2).toString());

            diy = (pws*pwg*pwg*pwg)/3+(sg*ss*ss*ss)/3+(ms*mg*mg*mg)/12+ms*mg*(ss+mg/2)*(ss+mg/2)-da*de*de;
            BigDecimal bd = new BigDecimal(diy);
            iy.setText(bd.setScale(2, RoundingMode.HALF_UP).toString());

            dw = (ss + mg - de) == 0 ? 0 : diy/(ss + mg - de);
            w.setText(round(dw, 2).toString());

        } else if(cienkoscienny.isChecked() && !plaskownik.isChecked()) {

            /*******************
             * NOT IMPLEMENTED YET
             *******************/

        } else if(!cienkoscienny.isChecked() && !plaskownik.isChecked()) {
            double au = DataParser.a.get(mark) == null ? 0 : DataParser.a.get(mark);
            da = pws*pwg+au;
            a.setText(round(da, 2).toString());

            dat = 0.8*ss*sg;
            at.setText(round(dat, 2).toString());

            double dx = DataParser.dx.get(mark) == null ? 0 : DataParser.dx.get(mark);
            dsy = au*dx-pws*pwg*pwg/2;
            de = da == 0 ? 0 : dsy/da;;
            e.setText(round((de+pwg),2).toString());

            double ix = DataParser.ix.get(mark) == null ? 0 : DataParser.ix.get(mark);
            diy = pws*pwg*pwg*pwg/3+ix+au*dx*dx-da*de*de;
            BigDecimal bd = new BigDecimal(diy);
            iy.setText(bd.setScale(2, RoundingMode.HALF_UP).toString());

            dw = (ss-de) == 0 ? 0 : diy/(ss-de);
            w.setText(round(dw, 2).toString());
        }

        Double tmp = (double) Calculator.dataUnitFactor / (double) Calculator.resultsUnitFactor;
        Calculator.updateUnits(activity, tmp);

    }
}
