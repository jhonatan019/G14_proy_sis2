/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cine2;


public class Item {
    private int id;
    private String nombre;
    
    public Item(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
    
    public int getId() {
        return id;
    } 
}
