package com.exercicis;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Random;



public class Resolt0 {

   
    public static HashMap<String, HashMap<String, Object>> clients = new HashMap<>();
    public static ArrayList<HashMap<String, Object>> operacions = new ArrayList<>();

   
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public static boolean validarNom(String nom) {
        nom = nom.trim().toLowerCase(); 
    
        if (nom.isEmpty()) {
            return false;
        }
    
        String caractersAcceptats = " abcdefghijklmnopqrstuvwxyzàáèéìíòóùúäëïöüç";
    
        for (char car : nom.toCharArray()) {
            if (caractersAcceptats.indexOf(car) == -1) {
                return false;
            }
        }
        return true;
    }
    
    
    public static boolean validarEdat(int edat) {

        return (edat >= 18 && edat <= 100);
    }

   
    public static boolean validarFactors(String[] factors) {
        if (factors != null && factors.length == 2) {
            String tipus = factors[0];
            String risc = factors[1];

            if ("autònom".equals(tipus) && "risc baix".equals(risc)) {
                return false;
            }

            return ("autònom".equals(tipus) || "empresa".equals(tipus)) &&
                ("risc alt".equals(risc) || "risc mitjà".equals(risc) || "risc baix".equals(risc));
        }
        return false;
    }


    public static boolean validarDescompte(double descompte) {
        return (descompte >= 0 && descompte <= 20);
    }

    
    public static boolean validarTipusOperacio(String tipus) {
        String[] tipusValids = {
            "Declaració d'impostos", "Gestió laboral", "Assessoria fiscal",
            "Constitució de societat", "Modificació d'escriptures", 
            "Testament", "Gestió d'herències", "Acta notarial",
            "Contracte de compravenda", "Contracte de lloguer"
        };
        for (String tipusValid : tipusValids) {
            if (tipusValid.equals(tipus)) {
                return true;
            }
        }
        return false;
    }

    public static boolean validarClients(ArrayList<String> clientsLlista, ArrayList<String> clientsGlobals) {
        if (clientsLlista == null || clientsGlobals == null) {
            return false;
        }

        HashMap<String, Integer> clientCount = new HashMap<>();

        
        for (String client : clientsLlista) {
            if (clientCount.containsKey(client)) {
                clientCount.put(client, clientCount.get(client) + 1);
            } else {
                clientCount.put(client, 1);
            }
        }

        for (String client : clientsLlista) {
            if (clientCount.get(client) > 1 || !clientsGlobals.contains(client)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAllDigits(String str) {
        if (str.length() == 0) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean validarData(String data) {
        if (data == null || data.length() != 10) {
            return false;
        }

        
        if (data.charAt(4) != '-' || data.charAt(7) != '-') {
            return false;
        }

        String anyStr = data.substring(0, 4);
        String mesStr = data.substring(5, 7);
        String diaStr = data.substring(8, 10);

        
        if (!isAllDigits(anyStr) || !isAllDigits(mesStr) || !isAllDigits(diaStr)) {
            return false;
        }

        int any = Integer.parseInt(anyStr);
        int mes = Integer.parseInt(mesStr);
        int dia = Integer.parseInt(diaStr);

        if (any < 1000 || any > 9999) {
            return false;
        }
        if (mes < 1 || mes > 12) {
            return false;
        }
        if (dia < 1 || dia > 31) {
            return false;
        }

        
        if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia > 30) {
            return false;
        }

        
        if (mes == 2 && dia > 29) {
            return false;
        }

        return true;
    }


    public static boolean validarPreu(double preu) {
        return preu > 100;
    }


    public static String generaClauClient() {
        Random random = new Random();
        String clau;

        do {
            int numeroAleatori = 100 + random.nextInt(900);
            clau = "client_" + numeroAleatori;
        } while (clients.containsKey(clau));

        return clau;
    }


    public static String afegirClient(String nom, int edat, ArrayList<String> factors, double descompte) {

        String novaClau = generaClauClient();
    
        HashMap<String, Object> clientInfo = new HashMap<>();
        clientInfo.put("nom", nom);
        clientInfo.put("edat", edat);
        clientInfo.put("factors", factors);
        clientInfo.put("descompte", descompte);
    
        clients.put(novaClau, clientInfo);
    
        return novaClau;
    }


    public static String modificarClient(String clauClient, String camp, Object nouValor) {
        if (!clients.containsKey(clauClient)) {
            return "Client '" + clauClient + "' no existeix.";
        }

        HashMap<String, Object> client = (HashMap<String, Object>) clients.get(clauClient);
        if (!client.containsKey(camp)) {
            return "El camp " + camp + " no existeix.";
        }

        client.put(camp, nouValor);

        return "OK";
    }


    public static String esborrarClient(String clauClient) {
        if (!clients.containsKey(clauClient)) {
            return "Client amb clau " + clauClient + " no existeix.";
        }

        clients.remove(clauClient);
        return "OK";
    }


    public static ArrayList<HashMap<String, HashMap<String, Object>>> llistarClients(
            ArrayList<String> claus,
            HashMap<String, Object> condicions) {
        
        ArrayList<HashMap<String, HashMap<String, Object>>> resultat = new ArrayList<>();

        for (String clau : clients.keySet()) {
            if (!claus.contains(clau)) {
                continue;
            }

            HashMap<String, Object> dades = clients.get(clau);
            boolean coincideix = true;

            for (String key : condicions.keySet()) {
                Object valorEsperat = condicions.get(key);

                if (!dades.containsKey(key) || !dades.get(key).equals(valorEsperat)) {
                    coincideix = false;
                    break;
                }
            }

            if (coincideix) {
                HashMap<String, HashMap<String, Object>> clientValid = new HashMap<>();
                clientValid.put(clau, dades);
                resultat.add(clientValid);
            }
        }

        return resultat;
    }


    public static String generaClauOperacio() {
        Random random = new Random();
        String clau;
    
        boolean existeix;
        do {
            int numeroAleatori = 100 + random.nextInt(900); 
            clau = "operacio_" + numeroAleatori;
    
            
            existeix = false;
            for (HashMap<String, Object> operacio : operacions) {
                if (clau.equals(operacio.get("id"))) {
                    existeix = true;
                    break;
                }
            }
        } while (existeix);
    
        return clau;
    }


    public static String afegirOperacio(
            String tipus,
            ArrayList<String> clientsImplicats,
            String data,
            String observacions,
            double preu) {

        String nouId = generaClauOperacio();

        HashMap<String, Object> operacio = new HashMap<>();
        operacio.put("id", nouId);
        operacio.put("tipus", tipus);
        operacio.put("clients", clientsImplicats);
        operacio.put("data", data);
        operacio.put("observacions", observacions);
        operacio.put("preu", preu);

        operacions.add(operacio); 
        return nouId;
    }

    public static String modificarOperacio(String idOperacio, String camp, Object nouValor) {
        for (HashMap<String, Object> operacio : operacions) {
            if (operacio.get("id").equals(idOperacio)) {
                if (operacio.containsKey(camp)) {
                    operacio.put(camp, nouValor);
                    return "OK";
                } else {
                    return "El camp " + camp + " no existeix en l'operació.";
                }
            }
        }
        return "Operació amb id " + idOperacio + " no existeix.";
    }

    public static String esborrarOperacio(String idOperacio) {
        for (int i = 0; i < operacions.size(); i++) {
            HashMap<String, Object> operacio = operacions.get(i);
            if (operacio.get("id").equals(idOperacio)) {
                operacions.remove(i);
                return "OK";
            }
        }
        return "Operació amb id " + idOperacio + " no existeix.";
    }


    public static ArrayList<HashMap<String, Object>> llistarOperacions(
            ArrayList<String> ids,
            HashMap<String, Object> condicions) {

        ArrayList<HashMap<String, Object>> resultat = new ArrayList<>();

        for (HashMap<String, Object> operacio : operacions) {
            
            if (ids != null && !ids.isEmpty() && !ids.contains(operacio.get("id"))) {
                continue;
            }

            boolean coincideix = true;

            
            if (condicions != null && !condicions.isEmpty()) {
                for (String key : condicions.keySet()) {
                    if (!operacio.containsKey(key) || !operacio.get(key).equals(condicions.get(key))) {
                        coincideix = false;
                        break;
                    }
                }
            }

            if (coincideix) {
                resultat.add(operacio);
            }
        }

        return resultat;
    }


    public static ArrayList<HashMap<String, Object>> llistarOperacionsClient(String clauClient) {
        ArrayList<HashMap<String, Object>> resultat = new ArrayList<>();

        for (HashMap<String, Object> operacio : operacions) {
            ArrayList<String> clients = (ArrayList<String>) operacio.get("clients");
            if (clients != null && clients.contains(clauClient)) {
                resultat.add(operacio);
            }
        }

        return resultat;
    }

    public static String alineaColumnes(ArrayList<Object[]> columnes) {
        StringBuilder result = new StringBuilder();
        
        for (Object[] columna : columnes) {
            String text = (String) columna[0];
            String alineacio = (String) columna[1];
            int ample = (int) columna[2];
            
            
            if (text.length() > ample) {
                text = text.substring(0, ample);
            }
            
    
            int espais = ample - text.length();
            
            switch (alineacio) {
                case "left":
                    result.append(text);
                    result.append(" ".repeat(espais));
                    break;
                case "right":
                    result.append(" ".repeat(espais));
                    result.append(text);
                    break;
                case "center":
                    int espaisEsquerra = espais / 2;
                    int espaisDreta = espais - espaisEsquerra;
                    result.append(" ".repeat(espaisEsquerra));
                    result.append(text);
                    result.append(" ".repeat(espaisDreta));
                    break;
            }
        }
        
        return result.toString();
    }


    public static ArrayList<String> taulaOperacionsClient(String clauClient, String ordre) {
        Locale defaultLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.US);

            HashMap<String, Object> client = clients.get(clauClient);
            if (client == null) {
                ArrayList<String> error = new ArrayList<>();
                error.add("Client amb clau " + clauClient + " no existeix.");
                return error;
            }
    
            ArrayList<HashMap<String, Object>> operacionsClient = llistarOperacionsClient(clauClient);
            operacionsClient.sort((o1, o2) -> {
                Object val1 = o1.get(ordre);
                Object val2 = o2.get(ordre);
                return val1.toString().compareTo(val2.toString());
            });
    
            ArrayList<String> linies = new ArrayList<>();
            
            
            String nomEdat = client.get("nom") + ", " + client.get("edat");
            String factors = "[" + String.join(", ", (ArrayList<String>) client.get("factors")) + "]";
            
            
            ArrayList<Object[]> columnesCapcalera = new ArrayList<>();
            columnesCapcalera.add(new Object[]{nomEdat, "left", 25});
            columnesCapcalera.add(new Object[]{factors, "right", 30});
            linies.add(alineaColumnes(columnesCapcalera));
            
            
            linies.add("-".repeat(55));
            
            
            ArrayList<Object[]> columnesTitols = new ArrayList<>();
            columnesTitols.add(new Object[]{"Tipus", "left", 30});
            columnesTitols.add(new Object[]{"Data", "left", 10});
            columnesTitols.add(new Object[]{"Preu", "right", 15});
            linies.add(alineaColumnes(columnesTitols));
            
            double sumaPreus = 0.0;
            
            for (HashMap<String, Object> operacio : operacionsClient) {
                ArrayList<Object[]> columnesOperacio = new ArrayList<>();
                columnesOperacio.add(new Object[]{operacio.get("tipus").toString(), "left", 30});
                columnesOperacio.add(new Object[]{operacio.get("data").toString(), "left", 10});
                
                double preu = ((Number) operacio.get("preu")).doubleValue();
                columnesOperacio.add(new Object[]{String.format("%.2f", preu), "right", 15});
                
                linies.add(alineaColumnes(columnesOperacio));
                sumaPreus += preu;
            }
            
            
            linies.add("-".repeat(55));
            
            
            int descomptePercentatge = 10;
            double percentatge = (100 - descomptePercentatge);
            double preuDescomptat = sumaPreus * (percentatge / 100.0); 
            double impostos = preuDescomptat * 0.21;
            double total = preuDescomptat + impostos;
    
            
            ArrayList<Object[]> columnesTotals = new ArrayList<>();
            columnesTotals.add(new Object[]{String.format("Suma: %.2f", sumaPreus), "right", 55});
            linies.add(alineaColumnes(columnesTotals));
    
            
            ArrayList<Object[]> columnesDescompte = new ArrayList<>();
            columnesDescompte.add(new Object[]{String.format("Descompte: %d%%", descomptePercentatge), "left", 30});
            columnesDescompte.add(new Object[]{String.format("Preu: %.2f", preuDescomptat), "right", 25});
            linies.add(alineaColumnes(columnesDescompte));
    
            
            ArrayList<Object[]> columnesImpostos = new ArrayList<>();
            columnesImpostos.add(new Object[]{String.format("Impostos:  21%% (%.2f)", impostos), "left", 30});
            columnesImpostos.add(new Object[]{String.format("Total: %.2f", total), "right", 25});
            linies.add(alineaColumnes(columnesImpostos));
            
            return linies;
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }


    public static ArrayList<String> getCadenesMenu() {
        String menuText = """
=== Menú de Gestió de Notaria ===
1. Afegir client
2. Modificar client
3. Esborrar client
4. Llistar clients
5. Afegir operació
6. Modificar operació
7. Esborrar operació
8. Llistar operacions
0. Sortir
            """;
        String[] lines = menuText.split("\\R");
        return new ArrayList<>(Arrays.asList(lines));
    }

    public static ArrayList<String> getLlistarClientsMenu() {
        ArrayList<String> linies = new ArrayList<>();
        linies.add("=== Llistar Clients ===");
    
        if (clients.isEmpty()) {
            linies.add("No hi ha clients per mostrar.");
            return linies;
        }
    
        for (String clau : clients.keySet()) {
            linies.add(clau + ": " + clients.get(clau).toString());
        }
    
        return linies;
    }


    public static void dibuixarLlista(ArrayList<String> llista) {
        for (String linia : llista) {
            System.out.println(linia);
        }
    }
    
    public static String obtenirOpcio(Scanner scanner) {
        ArrayList<String> menu = getCadenesMenu();
        
        while (true) {
            System.out.print("Selecciona una opció (número o paraula clau): ");

            String opcio = scanner.nextLine();
            
       
            try {
                int index = Integer.parseInt(opcio);
                if (index == 0) {
                    return "Sortir";
                } else if (index > 0 && index < menu.size() - 1) {
                    return menu.get(index).substring(3).trim();
                }
            } catch (NumberFormatException e) {
                
            }
            
           
            String opcioNormalized = opcio.trim().toLowerCase().replace("ó", "o");
            
            for (int i = 0; i < menu.size(); i++) {
                String paraulaClau = menu.get(i).substring(3).trim();
                String paraulaClauNormalized = paraulaClau.toLowerCase().replace("ó", "o");
                
                if (paraulaClauNormalized.equals(opcioNormalized)) {
                    return paraulaClau;
                }
            }
            
            
            System.out.println("Opció no vàlida. Torna a intentar-ho.");
        }
    }

    public static String llegirNom(Scanner scanner) {
        System.out.print("Introdueix el nom del client: ");
        String nom = scanner.nextLine().trim();
        
        while (!validarNom(nom)) {
            System.out.println("Nom no vàlid. Només s'accepten lletres i espais.");
            System.out.print("Introdueix el nom del client: ");
            nom = scanner.nextLine().trim();
        }
        return nom;
    }

    public static int llegirEdat(Scanner scanner) {
        System.out.print("Introdueix l'edat del client (18-100): ");
        String edatInput = scanner.nextLine().trim();
        
        while (!isAllDigits(edatInput) || !validarEdat(Integer.parseInt(edatInput))) {
            System.out.println("Edat no vàlida. Introdueix un número entre 18 i 100.");
            System.out.print("Introdueix l'edat del client (18-100): ");
            edatInput = scanner.nextLine().trim();
        }
        return Integer.parseInt(edatInput);
    }
    
    public static ArrayList<String> llegirFactors(Scanner scanner) {
        ArrayList<String> factors = new ArrayList<>();
        
        System.out.print("Introdueix el primer factor ('autònom' o 'empresa'): ");
        String factor1 = scanner.nextLine().trim();
        while (!factor1.equals("autònom") && !factor1.equals("empresa")) {
            System.out.println("Factor no vàlid. Ha de ser 'autònom' o 'empresa'.");
            System.out.print("Introdueix el primer factor ('autònom' o 'empresa'): ");
            factor1 = scanner.nextLine().trim();
        }
        factors.add(factor1);
        
        String promptFactor2 = factor1.equals("autònom")
                ? "Introdueix el segon factor ('risc alt' o 'risc mitjà'): "
                : "Introdueix el segon factor ('risc alt', 'risc baix' o 'risc mitjà'): ";
        
        System.out.print(promptFactor2);
        String factor2 = scanner.nextLine().trim();
        while (true) {
            if (factor1.equals("autònom")) {
                if (factor2.equals("risc alt") || factor2.equals("risc mitjà")) break;
                System.out.println("Factor no vàlid. Per a autònoms només pot ser 'risc alt' o 'risc mitjà'.");
            } else {
                if (factor2.equals("risc alt") || factor2.equals("risc baix") || factor2.equals("risc mitjà")) break;
                System.out.println("Factor no vàlid. Ha de ser 'risc alt', 'risc baix' o 'risc mitjà'.");
            }
            System.out.print(promptFactor2);
            factor2 = scanner.nextLine().trim();
        }
        factors.add(factor2);
        
        return factors;
    }
    
    public static double llegirDescompte(Scanner scanner) {
        System.out.print("Introdueix el descompte (0-20): ");
        String descompteInput = scanner.nextLine().trim();
        
        while (!descompteInput.matches("\\d+(\\.\\d+)?") || !validarDescompte(Double.parseDouble(descompteInput))) {
            System.out.println("Descompte no vàlid. Ha de ser un número entre 0 i 20.");
            System.out.print("Introdueix el descompte (0-20): ");
            descompteInput = scanner.nextLine().trim();
        }
        
        return Double.parseDouble(descompteInput);
    }


    public static ArrayList<String> afegirClientMenu(Scanner scanner) {
        ArrayList<String> linies = new ArrayList<>();
        linies.add("=== Afegir Client ===");
    
        String nom = llegirNom(scanner);
        int edat = llegirEdat(scanner);
        ArrayList<String> factors = llegirFactors(scanner);
        
        if (!validarFactors(factors.toArray(new String[0]))) {
            linies.add("Els factors no són vàlids.");
            return linies;
        }
        
        double descompte = llegirDescompte(scanner);
        
        String novaClau = afegirClient(nom, edat, factors, descompte);
        linies.add("S'ha afegit el client amb clau " + novaClau + ".");
        return linies;
    }
    

    public static ArrayList<String> modificarClientMenu(Scanner scanner) {
        ArrayList<String> linies = new ArrayList<>();
        linies.add("=== Modificar Client ===");
    
        System.out.print("Introdueix la clau del client a modificar (per exemple, 'client_100'): ");
        String clauClient = scanner.nextLine().trim();
        if (!clients.containsKey(clauClient)) {
            linies.add("Client amb clau " + clauClient + " no existeix.");
            return linies;
        }
    

        linies.add("Camps disponibles per modificar: nom, edat, factors, descompte");
        System.out.print("Introdueix el camp que vols modificar: ");
        String camp = scanner.nextLine().trim();
        if (!Arrays.asList("nom", "edat", "factors", "descompte").contains(camp)) {
            linies.add("El camp " + camp + " no és vàlid.");
            return linies;
        }

        Object nouValor = switch (camp) {
            case "nom" -> llegirNom(scanner);
            case "edat" -> llegirEdat(scanner);
            case "factors" -> {
                ArrayList<String> factors = llegirFactors(scanner);
                if (!validarFactors(factors.toArray(new String[0]))) {
                    linies.add("Els factors no són vàlids.");
                    yield null;
                }
                yield factors;
            }
            case "descompte" -> llegirDescompte(scanner);
            default -> null;
        };
    
        if (nouValor == null) return linies;
    
        String resultat = modificarClient(clauClient, camp, nouValor);
        if (!resultat.equals("OK")) {
            linies.add(resultat);
        } else {
            linies.add("S'ha modificat el client " + clauClient + ".");
        }
    
        return linies;
    }

    public static ArrayList<String> esborrarClientMenu(Scanner scanner) {
        ArrayList<String> linies = new ArrayList<>();
        linies.add("=== Esborrar Client ===");

        System.out.print("Introdueix la clau del client a esborrar (per exemple, 'client_100'): ");
        String clauClient = scanner.nextLine().trim();

        if (!clients.containsKey(clauClient)) {
            linies.add("Client amb clau " + clauClient + " no existeix.");
            return linies;
        }

        String resultat = esborrarClient(clauClient);
        if (!resultat.equals("OK")) {
            linies.add(resultat);
        } else {
            linies.add("S'ha esborrat el client " + clauClient + ".");
        }

        return linies;
    }

    public static void gestionaClientsOperacions(Scanner scanner) {

        ArrayList<String> menu = getCadenesMenu();
        ArrayList<String> resultat = new ArrayList<>();
        while (true) {
            
            clearScreen();
            dibuixarLlista(menu);
            dibuixarLlista(resultat);

            String opcio = obtenirOpcio(scanner);

            switch (opcio.toLowerCase(Locale.ROOT)) {
                case "sortir":
                    dibuixarLlista(new ArrayList<>(List.of("Fins aviat!")));
                    return;

                case "afegir client":
                    resultat = afegirClientMenu(scanner);
                    break;

                case "modificar client":
                    resultat = modificarClientMenu(scanner);
                    break;

                case "esborrar client":
                    resultat = esborrarClientMenu(scanner);
                    break;

                case "llistar clients":
                    resultat = getLlistarClientsMenu();
                    break;

                default:
                    resultat = new ArrayList<>(List.of("Opció no vàlida. Torna a intentar-ho."));
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        gestionaClientsOperacions(scanner);

        scanner.close();
    }
}
