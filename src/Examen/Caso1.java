package Examen;

import java.util.Random;

public class Caso1 implements Runnable{
	Entrada     entrada;
    Almacen     almacen;
    String      nombre;
    Random      generador;
    final int MAX_INTENTOS  =   10;
    
    public Caso1(Entrada e, Almacen a, String nombre){
            this.entrada    =   e;
            this.almacen    =   a;
            this.nombre     =   nombre;
            generador       =   new Random();
    }

    public void espera(){
            try {
                    int ms_azar = generador.nextInt(100);
                    Thread.sleep(ms_azar);
            } catch (InterruptedException ex) {
                    System.out.println("Falló la espera");
            }
    }
    
    @Override
    public void run() {
            for (int i=0; i<MAX_INTENTOS; i++){
                    if (!entrada.estaOcupada()){
                            if (entrada.intentarEntrar()){
                                    espera();
                                    entrada.liberarPuerta();
                                    
                                    if (almacen.cogerProducto()){
                                            System.out.println(this.nombre + ": cogí un producto");
                                            return ;
                                    }
                                    else {
                                            System.out.println(this.nombre+ ": crucé pero no cogí nada");
                                            return ;
                                    } 
                            } 
                    } else{
                       espera();
                    }
            } 
            System.out.println(this.nombre+ ": no pude coger un producto");
    }
    
    
    static class Almacen {
    	
        private int numProductos;
        public Almacen(int nProductos){
                this.numProductos=nProductos;
        }
        public boolean cogerProducto(){
                if (this.numProductos>0){
                        this.numProductos--;
                        return true;
                }
                return false;
        }
    }
    
    
    
    static class Entrada {
    	
        boolean ocupada;

        Entrada(){
                this.ocupada=false;
        }
        
        public boolean estaOcupada(){
                return this.ocupada;
        }
        
        public synchronized void liberarPuerta(){
                this.ocupada=false;
        }
        
        public synchronized boolean intentarEntrar(){
                if (this.ocupada) return false;
                this.ocupada=true;
                return true;
        }
    }
    
    
    
    static class GrandesAlmacenes {
        public static void main(String[] args) throws InterruptedException {
        	
                final int NUM_CLIENTES  = 300;
                final int NUM_PRODUCTOS = 100;

                Caso1[]   cliente =   new Caso1[NUM_CLIENTES];
                Almacen     almacen =   new Almacen(NUM_PRODUCTOS);
                Entrada      entrada  =   new Entrada();

                Thread[]    hilosAsociados=new Thread[NUM_CLIENTES];

                for (int i=0; i<NUM_CLIENTES; i++){
                        String nombreHilo   = "Cliente "+i;
                        cliente[i]          = new Caso1(entrada, almacen, nombreHilo);
                        hilosAsociados[i]   = new Thread(cliente[i]);
                        hilosAsociados[i].start();
                } 

                for (int i=0; i<NUM_CLIENTES; i++){
                        hilosAsociados[i].join();
                }
        	}
    }

}


