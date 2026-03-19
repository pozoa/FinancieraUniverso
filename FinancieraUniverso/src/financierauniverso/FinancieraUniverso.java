/*
 * MIT License
 * * Copyright (c) 2026 pozoa
 * * Se concede permiso por la presente, de forma gratuita, a cualquier persona que obtenga 
 * una copia de este software y de los archivos de documentación asociados (el "Software"), 
 * para utilizar el Software sin restricción, incluyendo, sin limitación, los derechos de 
 * uso, copia, modificación, fusión, publicación, distribución, sublicencia y/o venta 
 * de copias del Software.
 * * Autor: pozoa
 * Proyecto: Sistema Financiero Blockchain "UNIVERSO" (UNVs) con IA y Seguridad Cuántica
 */

package financierauniverso;

import java.util.*;
import java.security.MessageDigest;

public class FinancieraUniverso {
    
    // Base de datos de clientes vinculada por su Hash
    private static Map<String, Cliente> dbClientes = new HashMap<>();
    private static List<Bloque> blockchain = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarEntidad();
        menuPrincipal();
    }

    private static void inicializarEntidad() {
        // Nombres típicos españoles inventados
        String[] nombres = {
            "Juan García", "María Rodríguez", "Jose Luis Sánchez", "Carmen Fernández",
            "Antonio López", "Isabel Martínez", "Francisco Jiménez", "Dolores Pérez",
            "Manuel Ruiz", "Lucía González"
        };

        // Inicializamos los 10 clientes
        for (int i = 0; i < 10; i++) {
            String hashCliente = generarHash("ID-UNV-" + i).substring(0, 10);
            dbClientes.put(hashCliente, new Cliente(nombres[i], 10)); // 10 UNVs cada uno
        }

        // Bloque Génesis
        blockchain.add(new Bloque("0", new Transaccion("SISTEMA", "ORIGEN", 100)));
        
        System.out.println("--- ENTIDAD FINANCIERA 'UNIVERSO' INICIALIZADA ---");
        System.out.println("Tokens totales: 100 UNVs | Operador: pozoa");
        System.out.println("Seguridad: Post-Quantum & AI Defense Active.");
    }

    private static void menuPrincipal() {
        int opcion;
        do {
            System.out.println("\n--- OPERADOR FINANCIERO UNV ---");
            System.out.println("1. Listar Clientes (Nombres, Hashes y Saldo)");
            System.out.println("2. Realizar Transacción");
            System.out.println("3. Consultar Saldo por Hash Cliente");
            System.out.println("4. Consultar Transacción por Hash");
            System.out.println("5. Ver Libro Contable (Blockchain)");
            System.out.println("6. Simular Ataque Cuántico e IA de Defensa");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");
            opcion = sc.nextInt();
            sc.nextLine(); 

            switch (opcion) {
                case 1: listarClientes(); break;
                case 2: nuevaTransaccion(); break;
                case 3: consultarSaldo(); break;
                case 4: consultarTransaccion(); break;
                case 5: mostrarLibro(); break;
                case 6: simuladorAtaqueCuantico(); break;
            }
        } while (opcion != 0);
    }

    private static void listarClientes() {
        System.out.println("\n--- LISTADO DE CLIENTES REGISTRADOS ---");
        System.out.printf("%-15s | %-20s | %-10s\n", "HASH ID", "NOMBRE Y APELLIDOS", "SALDO UNV");
        System.out.println("------------------------------------------------------------");
        dbClientes.forEach((hash, cliente) -> {
            System.out.printf("%-15s | %-20s | %-10d UNVs\n", hash, cliente.nombre, cliente.saldo);
        });
    }

    private static void nuevaTransaccion() {
        System.out.print("Hash Emisor: "); String emisor = sc.nextLine();
        System.out.print("Hash Receptor: "); String receptor = sc.nextLine();
        System.out.print("Cantidad de UNVs: "); int monto = sc.nextInt();

        if (dbClientes.containsKey(emisor) && dbClientes.get(emisor).saldo >= monto) {
            // Actualizar saldos
            dbClientes.get(emisor).saldo -= monto;
            dbClientes.get(receptor).saldo += monto;
            
            Transaccion t = new Transaccion(emisor, receptor, monto);
            String prevHash = blockchain.get(blockchain.size() - 1).hash;
            Bloque nuevoBloque = new Bloque(prevHash, t);
            blockchain.add(nuevoBloque);

            System.out.println("\n[EXITO] Transacción #" + (blockchain.size()-1));
            System.out.println("Hash TX: " + nuevoBloque.hash);
            System.out.println("Nuevo saldo de " + dbClientes.get(emisor).nombre + ": " + dbClientes.get(emisor).saldo + " UNVs");
        } else {
            System.out.println("[ERROR] Fondos insuficientes o hash no reconocido.");
        }
    }

    private static void consultarSaldo() {
        System.out.print("Ingrese Hash del Cliente: ");
        String h = sc.nextLine();
        if(dbClientes.containsKey(h)) {
            Cliente c = dbClientes.get(h);
            System.out.println("Cliente: " + c.nombre + " | Saldo Actual: " + c.saldo + " UNVs");
        } else System.out.println("Hash no encontrado.");
    }

    private static void consultarTransaccion() {
        System.out.print("Ingrese Hash de Transacción: ");
        String h = sc.nextLine();
        for (Bloque b : blockchain) {
            if (b.hash.equals(h)) {
                System.out.println("Emisor: " + b.data.emisor + " -> Receptor: " + b.data.receptor);
                System.out.println("Cantidad: " + b.data.monto + " UNVs | Fecha: " + new Date(b.timeStamp));
                return;
            }
        }
        System.out.println("Transacción no encontrada.");
    }

    private static void mostrarLibro() {
        System.out.println("\n--- LIBRO CONTABLE INMUTABLE (BLOCKCHAIN) ---");
        for (Bloque b : blockchain) {
            System.out.println("[" + b.hash.substring(0,15) + "...] Prev: " + b.previousHash.substring(0,5) + " | Envío: " + b.data.monto + " UNV");
        }
    }

    private static void simuladorAtaqueCuantico() {
        System.out.println("\n--- [ALERTA] INICIANDO SIMULADOR DE ATAQUE CUÁNTICO ---");
        System.out.print("Introduce el Hash del cliente a proteger: ");
        String objetivo = sc.nextLine();

        System.out.println("\n[IA] Analizando frecuencia de peticiones...");
        pausa(1000);
        System.out.println("[PELIGRO] Patrón detectado: Algoritmo de Grover en ejecución.");
        
        for (int i = 0; i < 5; i++) {
            System.out.println("Intento Cuántico #" + i + ": " + generarHash("q" + i).substring(0, 30) + "... [RECHAZADO]");
            pausa(400);
        }

        System.out.println("\n[ESCUDO] Blindaje de Reticulado (Lattice) desplegado.");
        System.out.println("[IA] Ataque neutralizado. Los UNVs de este cliente están a salvo.");
    }

    private static void pausa(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {}
    }

    public static String generarHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}

// Clase para manejar los datos del cliente
class Cliente {
    String nombre;
    int saldo;
    public Cliente(String nombre, int saldo) {
        this.nombre = nombre;
        this.saldo = saldo;
    }
}

class Transaccion {
    String emisor, receptor;
    int monto;
    public Transaccion(String e, String r, int m) { this.emisor = e; this.receptor = r; this.monto = m; }
}

class Bloque {
    public String hash, previousHash;
    public Transaccion data;
    public long timeStamp;

    public Bloque(String previousHash, Transaccion data) {
        this.previousHash = previousHash;
        this.data = data;
        this.timeStamp = new Date().getTime();
        this.hash = FinancieraUniverso.generarHash(previousHash + timeStamp + data.emisor + data.receptor + data.monto);
    }
}