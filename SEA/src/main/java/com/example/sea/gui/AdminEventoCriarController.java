package com.example.sea.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class AdminEventoCriarController {

    @FXML
    private TextField eventoNomeField;

    @FXML
    private TextField eventoDataInicioField;


    public void novaFuncao (){
        String textoDigitado = eventoNomeField.getText();
        System.out.println(textoDigitado);
    }
}
