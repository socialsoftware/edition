package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.ArrayList;
import java.util.List;

public class FragInterRequestBodyDto {
    private ArrayList<String> selectedVE;
    private boolean diff;
    private boolean del;
    private boolean ins;
    private boolean sub;
    private boolean note;
    private boolean fac;
    private boolean line;
    private boolean align;
    private String pbText;
    private List<String> inters;

    public FragInterRequestBodyDto() {
    }

    public List<String> getInters() {
        return inters;
    }

    public void setInters(List<String> inters) {
        this.inters = inters;
    }

    public ArrayList<String> getSelectedVE() {
        return selectedVE;
    }

    public void setSelectedVE(ArrayList<String> selectedVE) {
        this.selectedVE = selectedVE;
    }

    public boolean isDiff() {
        return diff;
    }

    public void setDiff(boolean diff) {
        this.diff = diff;
    }

    public boolean isDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }

    public boolean isIns() {
        return ins;
    }

    public void setIns(boolean ins) {
        this.ins = ins;
    }

    public boolean isSub() {
        return sub;
    }

    public void setSub(boolean sub) {
        this.sub = sub;
    }

    public boolean isNote() {
        return note;
    }

    public void setNote(boolean note) {
        this.note = note;
    }

    public boolean isFac() {
        return fac;
    }

    public void setFac(boolean fac) {
        this.fac = fac;
    }

    public boolean isLine() {
        return line;
    }

    public void setLine(boolean line) {
        this.line = line;
    }

    public boolean isAlign() {
        return align;
    }

    public void setAlign(boolean align) {
        this.align = align;
    }

    public String getPbText() {
        return pbText;
    }

    public void setPbText(String pbText) {
        this.pbText = pbText;
    }
}
