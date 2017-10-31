/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//mascara generica utilizada para qualquer campo
//exemplo:<input onKeyUp="mascaraTexto(event,'999.999.999-99')">
function mascaraTexto(evento, mascara) {

    var campo, valor, i, tam, caracter;

    if (document.all) // Internet Explorer
        campo = evento.srcElement;
    else // Nestcape, Mozila
        campo = evento.target;

    valor = campo.value;
    tam = valor.length;

    for (i = 0; i < mascara.length; i++) {
        caracter = mascara.charAt(i);
        if (caracter != "#")
            if (i < tam & caracter != valor.charAt(i))
                campo.value = valor.substring(0, i) + caracter + valor.substring(i, tam);

    }

}
