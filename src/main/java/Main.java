import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;

import java.util.*;

public class Main {

    Random rand = new Random();

    public static void main(String[] args) {
        OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
        ODatabaseSession db = orient.open("demo", "admin", "admin");

        criaClasses(db);
        criaArestas(db);
        criaInstanciasSimples(db);
        criaInstanciasComplexo(db);

        db.close();
        orient.close();
    }

    public static void criaArestas(ODatabaseSession db) {
        // Arestas
        db.createEdgeClass("Usa");
        db.createEdgeClass("Segue");
        db.createEdgeClass("Sabe");
    }

    public static void criaClasses(ODatabaseSession db) {
        //Pessoa
        OClass pessoa = db.createVertexClass("Pessoa");

        pessoa.createProperty("nome", OType.STRING);
        pessoa.createProperty("sobrenome", OType.STRING);
        pessoa.createProperty("idade", OType.INTEGER);

        //Tecnologia
        OClass tecnologia = db.createVertexClass("Tecnologia");

        tecnologia.createProperty("nome", OType.STRING);

        //Empresa
        OClass empresa = db.createVertexClass("Empresa");

        tecnologia.createProperty("nome", OType.STRING);


    }


    public static void criaInstanciasSimples(ODatabaseSession db) {
        //Criando Pessoas
        OVertex alice = criaPessoa(db, "Alice", "Fagundes", 20);
        OVertex jose = criaPessoa(db, "Jose", "Silva", 25);
        OVertex maria = criaPessoa(db, "Maria", "Souza", 30);

        OEdge edge1 = alice.addEdge(jose, "Segue");
        edge1.save();
        OEdge edge2 = maria.addEdge(jose, "Segue");
        edge2.save();

        // Criando  tecnologias
        OVertex tecnologia = db.newVertex("Tecnologia");
        tecnologia.setProperty("nome", "React");
        tecnologia.save();

        OVertex tecnologia2 = db.newVertex("Tecnologia");
        tecnologia2.setProperty("nome", "Java");
        tecnologia2.save();

        OVertex tecnologia3 = db.newVertex("Tecnologia");
        tecnologia3.setProperty("nome", "Vue");
        tecnologia3.save();

        OEdge edge3 = alice.addEdge(tecnologia, "Sabe");
        edge3.save();
        OEdge edge4 = alice.addEdge(tecnologia2, "Sabe");
        edge4.save();
        OEdge edge5 = jose.addEdge(tecnologia3, "Sabe");
        edge5.save();

        // Criando empresas
        OVertex empresa = db.newVertex("Empresa");
        empresa.setProperty("nome", "Netflix");
        empresa.save();

        OEdge edge6 = empresa.addEdge(tecnologia, "Usa");
        edge6.save();

        OVertex empresa2 = db.newVertex("Empresa");
        empresa2.setProperty("nome", "Google");
        empresa2.save();

        OEdge edge7 = empresa2.addEdge(tecnologia2, "Usa");
        edge7.save();

        OEdge edge8 = empresa2.addEdge(tecnologia3, "Usa");
        edge8.save();
    }

    private static void criaInstanciasComplexo(ODatabaseSession db) {
        String[] nomes = new String[]{"Pedro", "Matheus", "Mireli", "Joaquim", "Giovanni",
                "Deborah", "Joyce", "Sheyla", "Samara", "Italo", "Higor"};
        String[] sobrenomes = new String[]{"A.", "B.", "C.", "D.", "E.",
                "F.", "G.", "H.", "L.", "M.", "N."};
        ArrayList<OVertex> pessoas = new ArrayList<OVertex>();

        OVertex pessoa = null;
        for (int i = 0; i < nomes.length; i++) {
            pessoa = criaPessoa(db, nomes[i], sobrenomes[i], i * 2 + 15);
            pessoas.add(pessoa);
        }

        //Cria Ligações - seguidores

        Random rand = new Random();
        for (int i = 0; i < pessoas.size(); i++) {
            int quantidade = rand.nextInt(pessoas.size() - 1);

            Integer[] seguir = listaDeIntsAleatorios(pessoas.size() - 1, quantidade);

            for (int j = 0; j < seguir.length; j++) {
                OEdge edge = pessoas.get(i).addEdge(pessoas.get(seguir[j]), "Segue");
                edge.save();
            }
        }

        // Criando Pessoas 'Sabem' Tecnologia


        OVertex oracle = db.newVertex("Tecnologia");
        oracle.setProperty("nome", "Oracle");
        oracle.save();

        OVertex r = db.newVertex("Tecnologia");
        r.setProperty("nome", "R");
        r.save();

        OVertex php = db.newVertex("Tecnologia");
        php.setProperty("nome", "PHP");
        php.save();


        for (int i = 0; i < pessoas.size(); i++) {
            if (i % 2 == 0) {
                OEdge edge = pessoas.get(i).addEdge(php, "Sabe");
                edge.save();
                OEdge edge2 = pessoas.get(i).addEdge(r, "Sabe");
                edge2.save();
            } else {
                OEdge edge = pessoas.get(i).addEdge(oracle, "Sabe");
                edge.save();
            }
        }

        // Empresas 'Usam'
        OVertex empresa = db.newVertex("Empresa");
        empresa.setProperty("nome", "Microsoft");
        empresa.save();

        OVertex empresa2 = db.newVertex("Empresa");
        empresa2.setProperty("nome", "Adidas");
        empresa2.save();

        OVertex empresa3 = db.newVertex("Empresa");
        empresa3.setProperty("nome", "Amazon");
        empresa3.save();

        OEdge edge1 = empresa.addEdge(oracle, "Usa");
        edge1.save();
        OEdge edge2 = empresa.addEdge(r, "Usa");
        edge2.save();

        OEdge edge3 = empresa2.addEdge(oracle, "Usa");
        edge3.save();
        OEdge edge4 = empresa2.addEdge(php, "Usa");
        edge4.save();

        OEdge edge5 = empresa3.addEdge(php, "Usa");
        edge5.save();
        OEdge edge6 = empresa3.addEdge(r, "Usa");
        edge6.save();

    }

    private static OVertex criaPessoa(ODatabaseSession db, String nome, String sobrenome, int idade) {
        OVertex pessoa = db.newVertex("Pessoa");
        pessoa.setProperty("nome", nome);
        pessoa.setProperty("sobrenome", sobrenome);
        pessoa.setProperty("idade", idade);
        pessoa.save();
        return pessoa;
    }

    private static Integer[] listaDeIntsAleatorios(int superior, int quantidade) {

        Set<Integer> lista = new HashSet<Integer>();
        Random rand = new Random();

        for (int i = 0; i < quantidade; i++) {
            int random = rand.nextInt(superior);
            lista.add(random);
        }

        Integer[] myArray = new Integer[lista.size()];
        lista.toArray(myArray);
        return myArray;
    }

}