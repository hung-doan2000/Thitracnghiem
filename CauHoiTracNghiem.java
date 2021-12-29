/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChuongTrinhTracNghiem;

import java.io.Serializable;

/**
 *
 * @author ADMIN
 */
public class CauHoiTracNghiem implements Serializable{
    int id;
    String cauHoi;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDA() {
        return DA;
    }

    public void setDA(String DA) {
        this.DA = DA;
    }
    String cauA;
    String cauB;
    String cauC;
    String cauD;
    String DA;

    public CauHoiTracNghiem() {
    }

    public String getCauHoi() {
        return cauHoi;
    }

    public void setCauHoi(String cauHoi) {
        this.cauHoi = cauHoi;
    }

    public String getCauA() {
        return cauA;
    }

    public void setCauA(String cauA) {
        this.cauA = cauA;
    }

    public String getCauB() {
        return cauB;
    }

    public void setCauB(String cauB) {
        this.cauB = cauB;
    }

    public String getCauC() {
        return cauC;
    }

    public void setCauC(String cauC) {
        this.cauC = cauC;
    }

    public String getCauD() {
        return cauD;
    }

    public void setCauD(String cauD) {
        this.cauD = cauD;
    }
    
    
}
