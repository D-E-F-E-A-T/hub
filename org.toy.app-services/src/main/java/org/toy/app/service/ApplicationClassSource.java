package org.toy.app.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.toy.asm.ClassHelper;
import org.toy.stdlib.collections.itertools.ChainIterator;
import org.toy.asm.ClassNode;

public class ApplicationClassSource extends ClassSource {

	private final String name;
	private final List<LibraryClassSource> libraries;
	private ClassTree classTree;
	
	public ApplicationClassSource(String name, Collection<ClassNode> classes) {
		this(name, ClassHelper.convertToMap(classes));
	}
	
	public ApplicationClassSource(String name, Map<String, ClassNode> nodeMap) {
		super(nodeMap);
		this.name = (name == null ? "unknown" : name);
		libraries = new ArrayList<>();
	}
	
	public List<LibraryClassSource> getLibraries() {
		return libraries;
	}

	protected ClassTree _getClassTree() {
		return classTree;
	}
	
	public ClassTree getClassTree() {
		if (classTree == null) {
			classTree = new ClassTree(this);
			classTree.init();
		}
		return classTree;
	}
	
	@Override
	public void rebuildTable() {
		// rebuild app table
		super.rebuildTable();
		// rebuild lib tables
		for(LibraryClassSource lib : libraries) {
			lib.rebuildTable();
		}
	}
	
	public void addLibraries(LibraryClassSource... libs) {
		for(LibraryClassSource cs : libs) {
			if(!libraries.contains(cs)) {
				libraries.add(cs);
			}
		}
	}
	
	public ClassNode findClassNode(String name) {
		LocateableClassNode n = findClass(name);
		
		if(n != null) {
			ClassNode cn = n.node;
			return cn;
		} else {
			return null;
		}
	}
	
	@Override
	public LocateableClassNode findClass(String name) {
		if(name == null) {
			return null;
		}
		
		LocateableClassNode node = findClass0(name);
		
		if(node != null) {
			return node;
		} else {
			for(LibraryClassSource cs : libraries) {
				if(cs.contains(name)) {
					return cs.findClass0(name);
				}
			}
			return null;
		}
	}

	public boolean isLibraryClass(String name) {
		if(name == null) {
			return false;
		}
		
		/* quick check to see if it's app class instead. 
		 * (prevents attempted loading by runtime lib). */
		if(contains(name)) {
			return false;
		}
		
		for(LibraryClassSource cs : libraries) {
			if(cs.contains(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean isApplicationClass(String name) {
		if(name == null) {
			return false;
		}
		
		return contains(name);
	}

	public Iterable<ClassNode> iterateWithLibraries() {
		return iterateWithLibraries(false);
	}
	
	public Iterable<ClassNode> iterateWithLibraries(boolean force) {
		return new Iterable<ClassNode>() {
			Iterator<LibraryClassSource> libIt = null;
			@Override
			public Iterator<ClassNode> iterator() {
				return new ChainIterator<ClassNode>() {
					@Override
					public Iterator<ClassNode> nextIterator() {
						if(libIt == null) {
							libIt = libraries.iterator();
							return ApplicationClassSource.this.iterator();
						} else {
							if(libIt.hasNext()) {
								LibraryClassSource lib = libIt.next();
								if(lib.isIterable() || force) {
									return lib.iterator();
								} else {
									return nextIterator();
								}
							} else {
								return null;
							}
						}
					}
				};
			}
		};
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
