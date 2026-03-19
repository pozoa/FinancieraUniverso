/*
 * MIT License
 * * Copyright (c) 2026 pozoa
 * * Se concede permiso por la presente, de forma gratuita, a cualquier persona que obtenga 
 * una copia de este software y de los archivos de documentación asociados (el "Software"), 
 * para utilizar el Software sin restricción, incluyendo, sin limitación, los derechos de 
 * uso, copia, modificación, fusión, publicación, distribución, sublicencia y/o venta 
 * de copias del Software.
 * * Autor: pozoa
 * Proyecto: Sistema Financiero Blockchain "UNIVERSO" (UNVs)
 */

package financierauniverso;

/**
 *
 * @author delpo
 */
import java.util.*;
import java.security.MessageDigest;

public class FinancieraUniverso {
    
    // Base de datos simulada
    private static Map<String, Integer> saldosClientes = new HashMap<>();
    private static List<Bloque> blockchain = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarEntidad();
        menuPrincipal();
    }

    private static void inicializarEntidad() {
        // Creamos 10 clientes con hashes ficticios y repartimos los 100 UNVs
        for (int i = 1; i <= 10; i++) {
            String hashCliente = generarHash("Cliente" + i).substring(0, 10);
            saldosClientes.put(hashCliente, 10); // 10 tokens cada uno = 100 total
        }
        // Bloque Génesis (el inicio de la historia)
        blockchain.add(new Bloque("0", new Transaccion("SISTEMA", "ORIGEN", 100)));
        System.out.println("--- ENTIDAD FINANCIERA 'UNIVERSO' INICIALIZADA ---");
        System.out.println("Tokens totales: 100 UNVs | Clientes: 10");
    }

    private static void menuPrincipal() {
        int opcion;
        do {
            System.out.println("\n--- OPERADOR FINANCIERO UNV ---");
            System.out.println("1. Listar Clientes (Hashes)");
            System.out.println("2. Realizar Transacción");
            System.out.println("3. Consultar Saldo por Hash Cliente");
            System.out.println("4. Consultar Transacción por Hash");
            System.out.println("5. Ver Libro Contable (Blockchain)");
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
            }
        } while (opcion != 0);
    }

    private static void listarClientes() {
        System.out.println("\nLista de Clientes:");
        saldosClientes.forEach((hash, saldo) -> System.out.println("ID: " + hash + " | Saldo: " + saldo + " UNVs"));
    }

    private static void nuevaTransaccion() {
        System.out.print("Hash Emisor: "); String emisor = sc.nextLine();
        System.out.print("Hash Receptor: "); String receptor = sc.nextLine();
        System.out.print("Cantidad de UNVs: "); int monto = sc.nextInt();

        if (saldosClientes.containsKey(emisor) && saldosClientes.get(emisor) >= monto) {
            saldosClientes.put(emisor, saldosClientes.get(emisor) - monto);
            saldosClientes.put(receptor, saldosClientes.get(receptor) + monto);
            
            Transaccion t = new Transaccion(emisor, receptor, monto);
            String prevHash = blockchain.get(blockchain.size() - 1).hash;
            Bloque nuevoBloque = new Bloque(prevHash, t);
            blockchain.add(nuevoBloque);

            System.out.println("\n[EXITO] Transacción #" + (blockchain.size()-1));
            System.out.println("Hash Transacción: " + nuevoBloque.hash);
            System.out.println("Nuevo saldo de " + emisor + ": " + saldosClientes.get(emisor) + " UNVs");
        } else {
            System.out.println("[ERROR] Fondos insuficientes o hash inválido.");
        }
    }

    private static void consultarSaldo() {
        System.out.print("Ingrese Hash del Cliente: ");
        String h = sc.nextLine();
        if(saldosClientes.containsKey(h)) 
            System.out.println("El cliente " + h + " tiene: " + saldosClientes.get(h) + " UNVs");
        else System.out.println("Cliente no encontrado.");
    }

    private static void consultarTransaccion() {
        System.out.print("Ingrese Hash de Transacción: ");
        String h = sc.nextLine();
        for (Bloque b : blockchain) {
            if (b.hash.equals(h)) {
                System.out.println("De: " + b.data.emisor + " -> Para: " + b.data.receptor + " | Cantidad: " + b.data.monto);
                return;
            }
        }
        System.out.println("Transacción no encontrada.");
    }

    private static void mostrarLibro() {
        System.out.println("\n--- LIBRO CONTABLE INMUTABLE ---");
        for (Bloque b : blockchain) {
            System.out.println("Hash: " + b.hash + " | Prev: " + b.previousHash + " | TX: " + b.data.monto + " UNV");
        }
    }

    // Utilidad para generar Hashes SHA-256
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
