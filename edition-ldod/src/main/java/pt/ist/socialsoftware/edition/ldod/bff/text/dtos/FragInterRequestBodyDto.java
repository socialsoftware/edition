package pt.ist.socialsoftware.edition.ldod.bff.text.dtos;

import java.util.ArrayList;
import java.util.List;

public class FragInterRequestBodyDto {
    private boolean diff = false;
    private boolean del = false;
    private boolean ins = true;
    private boolean sub = false;
    private boolean note = true;
    private boolean fac = false;
    private boolean line = false;
    private boolean align = false;
    private String pbText = null;
    private List<String> inters = new ArrayList<>();

    public FragInterRequestBodyDto() {
    }

    public List<String> getInters() {
        return inters;
    }

    public void setInters(List<String> inters) {
        this.inters = inters;
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
