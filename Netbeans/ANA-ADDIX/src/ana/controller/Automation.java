/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ana.controller;

import org.openqa.selenium.WebDriver;
import vn.addix.utils.Selenium;

public class Automation extends Selenium{
    private WebDriver driver;
    public Automation(String chromePath) {
        super(chromePath);        
    }
    
}
