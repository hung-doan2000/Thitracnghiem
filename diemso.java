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
public class diemso implements Serializable{
    int cauDung;
    int cauSai;
    
    diemso(){
        
    }

    public int getCauDung() {
        return cauDung;
    }

    public void setCauDung(int cauDung) {
        this.cauDung = cauDung;
    }

    public int getCauSai() {
        return cauSai;
    }

    public void setCauSai(int cauSai) {
        this.cauSai = cauSai;
    }

}
