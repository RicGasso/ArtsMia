package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	private Map<Integer, ArtObject> idMap; 
	
	public Model() {
		dao = new ArtsmiaDAO();
		idMap = new HashMap<Integer, ArtObject>();
		//creaGrafo();
		
	}
	
	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungere i vertici
		//1 - recupero tutti gli ArtObject dal DB
		//2 - li inserisco come vertici
		dao.listObjects(idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		
		//Aggiungere gli archi
		// APPROCCIO 1
		// -> Doppio ciclo for sui vertici
		// -> Dati due vertici, controllo se sono collegati
		// -> Troppo complesso, tempo di risoluzione = 2 anni circa
		
		/*for(ArtObject a1 : this.grafo.vertexSet()) {
			for(ArtObject a2 : this.grafo.vertexSet()) {
				if(!a1.equals(a2) && !this.grafo.containsEdge(a1, a2)) {
					// devo collegare a1 ad a2? 
					int peso = dao.getpeso(a1, a2);
					if(peso>0) {
						Graphs.addEdge(this.grafo, a1, a2, peso);
					}
				}
			}
		}*/
		
		// APPROCCIO 2
		// -> Passo come input un oggetto e per ogni oggetto controllo do come output un elenco dei vicini con il rispettivo peso
		// -> Effettua solo più un ciclo for
		// -> Ancora troppo complesso, tempo di risoluzione = 30 min circa
		
		// APPROCCIO 3
		// -> Ottengo direttamente dal database l'elenco di tutti gli archi senza dover effettuare le ricerche
		// -> Li salvo in un insieme lato codice
		// -> Molto veloce anche su dati molto numerosi 
		for(Adiacenza a: dao.getAdiacenze()) {
				Graphs.addEdge(this.grafo, idMap.get(a.getId1()), idMap.get(a.getId2()), a.getPeso());
		}
		
		
		System.out.println("grafo creato");
		System.out.println("# vertici "+ grafo.vertexSet().size());
		System.out.println("# archi " + grafo.edgeSet().size());
	}
}
