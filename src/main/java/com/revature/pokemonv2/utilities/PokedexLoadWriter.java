package com.revature.pokemonv2.utilities;

import java.util.ArrayList;
import java.util.List;

import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

import com.revature.pokemonv2.dao.DAO;
import com.revature.pokemonv2.model.Pokemon;

public class PokedexLoadWriter implements CacheLoaderWriter {
	
	private static DAO dao = new DAO();
	private static final CachingUtility cachingUtility = CachingUtility.getCachingUtility();
	

	@Override
	public List<Pokemon> load(Object key) throws Exception {
		List<Pokemon> pokeDex = dao.getTrainerPokedex((String)key);
		List<Pokemon> returnPokeDex = new ArrayList<>();
		for (Pokemon p : pokeDex) {
			Pokemon poke = cachingUtility.getPokemonFromCache(p.getId());
			poke.setCount(p.getCount());
			returnPokeDex.add(poke);
		}
		
		return returnPokeDex;
	}

	@Override
	public void write(Object key, Object value) throws Exception {
		// TODO
	}

	@Override
	public void delete(Object key) throws Exception {
		// TODO
	}

}
