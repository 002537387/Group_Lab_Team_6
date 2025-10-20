/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Profiles;
import java.util.Date;
import java.text.SimpleDateFormat;
/**
 *
 * @author jim.hsieh
 */
public class PaymentRecord {
    private Date date;
    private double amount;
    private String semester;
    public PaymentRecord(Date date, double amount, String semester) {
        this.date = date;
        this.amount = amount;
        this.semester = semester;
    }
    
    /**
     * 取得格式化的日期
     */
    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    
    /**
     * 取得缴费金额
     */
    public double getAmount() {
        return amount;
    }
    
    /**
     * 取得学期
     */
    public String getSemester() {
        return semester;
    }
    
    @Override
    public String toString() {
        return getDate() + " - $" + String.format("%.2f", amount) + " (" + semester + ")";
    }
}
