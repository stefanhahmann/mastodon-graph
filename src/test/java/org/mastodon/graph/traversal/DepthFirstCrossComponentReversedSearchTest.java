package org.mastodon.graph.traversal;

import static org.mastodon.graph.algorithm.traversal.GraphSearch.EdgeClass.CROSS;
import static org.mastodon.graph.algorithm.traversal.GraphSearch.EdgeClass.TREE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mastodon.collection.RefCollections;
import org.mastodon.collection.RefList;
import org.mastodon.graph.TestEdge;
import org.mastodon.graph.TestVertex;
import org.mastodon.graph.algorithm.traversal.DepthFirstCrossComponentSearch;
import org.mastodon.graph.algorithm.traversal.GraphSearch.EdgeClass;
import org.mastodon.graph.algorithm.traversal.GraphSearch.SearchDirection;
import org.mastodon.graph.object.ObjectEdge;
import org.mastodon.graph.object.ObjectVertex;
import org.mastodon.graph.traversal.GraphsForTests.GraphTestBundle;
import org.mastodon.graph.traversal.GraphsForTests.TraversalTester;

public class DepthFirstCrossComponentReversedSearchTest
{

	@Test
	public void testTwoComponentsPoolObjects()
	{
		final GraphTestBundle< TestVertex, TestEdge > bundle = GraphsForTests.twoComponentsPoolObjects();

		final TestVertex first = bundle.vertices.get( 3 );
		final DepthFirstCrossComponentSearch< TestVertex, TestEdge > dfs = new DepthFirstCrossComponentSearch<>( bundle.graph, SearchDirection.REVERSED );

		final List< TestVertex > expectedVertices = Arrays.asList( new TestVertex[] {
				bundle.vertices.get( 3 ),
				bundle.vertices.get( 1 ),
				bundle.vertices.get( 0 ),

				bundle.vertices.get( 13 ),
				bundle.vertices.get( 12 ),
				bundle.vertices.get( 11 ),
				bundle.vertices.get( 10 ),
				bundle.vertices.get( 9 ),
				bundle.vertices.get( 8 ),
				bundle.vertices.get( 7 ),

				bundle.vertices.get( 6 ),
				bundle.vertices.get( 2 ),
				bundle.vertices.get( 4 ),
				bundle.vertices.get( 5 ),

		} );
		final List< TestVertex > processedVertices = Arrays.asList( new TestVertex[] {
				bundle.vertices.get( 0 ),
				bundle.vertices.get( 1 ),
				bundle.vertices.get( 3 ),

				bundle.vertices.get( 7 ),
				bundle.vertices.get( 8 ),
				bundle.vertices.get( 9 ),
				bundle.vertices.get( 10 ),
				bundle.vertices.get( 11 ),
				bundle.vertices.get( 12 ),
				bundle.vertices.get( 13 ),

				bundle.vertices.get( 2 ),
				bundle.vertices.get( 6 ),
				bundle.vertices.get( 5 ),
				bundle.vertices.get( 4 ),

		} );
		final List< TestEdge > expectedEdges = Arrays.asList( new TestEdge[] {
				bundle.edges.get( 3 ),
				bundle.edges.get( 0 ),

				bundle.edges.get( 12 ),
				bundle.edges.get( 11 ),
				bundle.edges.get( 10 ),
				bundle.edges.get( 9 ),
				bundle.edges.get( 8 ),
				bundle.edges.get( 7 ),

				bundle.edges.get( 6 ),
				bundle.edges.get( 1 ),
				bundle.edges.get( 2 ),

				bundle.edges.get( 5 ),
				bundle.edges.get( 4 ),

		} );
		final List< EdgeClass > edgeClass = Arrays.asList( new EdgeClass[] {
				TREE, TREE,
				TREE, TREE, TREE, TREE, TREE, TREE,
				TREE, CROSS, CROSS,
				TREE, CROSS } );

		final TraversalTester< TestVertex, TestEdge, DepthFirstCrossComponentSearch< TestVertex, TestEdge > > traversalTester =
				new TraversalTester<>(
				expectedVertices.iterator(),
				processedVertices.iterator(),
				expectedEdges.iterator(),
				edgeClass.iterator() );

		dfs.setTraversalListener( traversalTester );
		dfs.start( first );
		traversalTester.searchDone();
	}

	@Test
	public void testTwoComponentsStdObjects()
	{
		final GraphTestBundle< ObjectVertex< Integer >, ObjectEdge< Integer >> bundle = GraphsForTests.twoComponentsStdObjects();

		final ObjectVertex< Integer > first = bundle.vertices.get( 3 );
		final RefList< ObjectVertex< Integer > > leaves = RefCollections.createRefList( bundle.graph.vertices() );
		leaves.add( bundle.vertices.get( 13 ) );
		leaves.add( bundle.vertices.get( 6 ) );
		leaves.add( bundle.vertices.get( 4 ) );

		final DepthFirstCrossComponentSearch< ObjectVertex< Integer >, ObjectEdge< Integer > > dfs = new DepthFirstCrossComponentSearch<>( bundle.graph, SearchDirection.REVERSED, leaves );

		@SuppressWarnings( "unchecked" )
		final List< ObjectVertex< Integer > > expectedVertices = Arrays.asList( new ObjectVertex[] {
				bundle.vertices.get( 3 ),
				bundle.vertices.get( 1 ),
				bundle.vertices.get( 0 ),

				bundle.vertices.get( 13 ),
				bundle.vertices.get( 12 ),
				bundle.vertices.get( 11 ),
				bundle.vertices.get( 10 ),
				bundle.vertices.get( 9 ),
				bundle.vertices.get( 8 ),
				bundle.vertices.get( 7 ),

				bundle.vertices.get( 6 ),
				bundle.vertices.get( 2 ),

				bundle.vertices.get( 4 ),
				bundle.vertices.get( 5 ),

		} );
		@SuppressWarnings( "unchecked" )
		final List< ObjectVertex< Integer > > processedVertices = Arrays.asList( new ObjectVertex[] {
				bundle.vertices.get( 0 ),
				bundle.vertices.get( 1 ),
				bundle.vertices.get( 3 ),

				bundle.vertices.get( 7 ),
				bundle.vertices.get( 8 ),
				bundle.vertices.get( 9 ),
				bundle.vertices.get( 10 ),
				bundle.vertices.get( 11 ),
				bundle.vertices.get( 12 ),
				bundle.vertices.get( 13 ),

				bundle.vertices.get( 2 ),
				bundle.vertices.get( 6 ),
				bundle.vertices.get( 5 ),
				bundle.vertices.get( 4 ),

		} );
		@SuppressWarnings( "unchecked" )
		final List< ObjectEdge< Integer > > expectedEdges = Arrays.asList( new ObjectEdge[] {
				bundle.edges.get( 3 ),
				bundle.edges.get( 0 ),

				bundle.edges.get( 12 ),
				bundle.edges.get( 11 ),
				bundle.edges.get( 10 ),
				bundle.edges.get( 9 ),
				bundle.edges.get( 8 ),
				bundle.edges.get( 7 ),

				bundle.edges.get( 6 ),
				bundle.edges.get( 1 ),
				bundle.edges.get( 2 ),

				bundle.edges.get( 5 ),
				bundle.edges.get( 4 ),

		} );
		final List< EdgeClass > edgeClass = Arrays.asList( new EdgeClass[] {
				TREE, TREE,
				TREE, TREE, TREE, TREE, TREE, TREE,
				TREE, CROSS, CROSS,
				TREE, CROSS } );

		final TraversalTester< ObjectVertex< Integer >, ObjectEdge< Integer >, DepthFirstCrossComponentSearch< ObjectVertex< Integer >, ObjectEdge< Integer > > > traversalTester =
				new TraversalTester<>(
				expectedVertices.iterator(),
				processedVertices.iterator(),
				expectedEdges.iterator(),
				edgeClass.iterator() );

		dfs.setTraversalListener( traversalTester );
		dfs.start( first );
		traversalTester.searchDone();
	}

	@Test
	public void testmultipleComponentsPoolObjects()
	{
		final GraphTestBundle< TestVertex, TestEdge > bundle = GraphsForTests.multipleComponentsPoolObjects();

		final List< EdgeClass > edgeClass = new ArrayList<>();
		for ( int i = 0; i < 4; i++ )
			edgeClass.addAll( Arrays.asList( new EdgeClass[] { TREE, TREE, TREE, CROSS } ) );

		// Expected iteration order.
		final RefList< TestVertex > expected = RefCollections.createRefList( bundle.graph.vertices() );
		expected.add( bundle.vertices.get( 11 ) );
		expected.add( bundle.vertices.get( 9 ) );
		expected.add( bundle.vertices.get( 8 ) );
		expected.add( bundle.vertices.get( 10 ) );

		expected.add( bundle.vertices.get( 3 ) );
		expected.add( bundle.vertices.get( 1 ) );
		expected.add( bundle.vertices.get( 0 ) );
		expected.add( bundle.vertices.get( 2 ) );

		expected.add( bundle.vertices.get( 15 ) );
		expected.add( bundle.vertices.get( 13 ) );
		expected.add( bundle.vertices.get( 12 ) );
		expected.add( bundle.vertices.get( 14 ) );

		expected.add( bundle.vertices.get( 7 ) );
		expected.add( bundle.vertices.get( 5 ) );
		expected.add( bundle.vertices.get( 4 ) );
		expected.add( bundle.vertices.get( 6 ) );

		// Expected processed order.
		final RefList< TestVertex > processed = RefCollections.createRefList( bundle.graph.vertices() );
		processed.add( bundle.vertices.get( 8 ) );
		processed.add( bundle.vertices.get( 9 ) );
		processed.add( bundle.vertices.get( 10 ) );
		processed.add( bundle.vertices.get( 11 ) );

		processed.add( bundle.vertices.get( 0 ) );
		processed.add( bundle.vertices.get( 1 ) );
		processed.add( bundle.vertices.get( 2 ) );
		processed.add( bundle.vertices.get( 3 ) );

		processed.add( bundle.vertices.get( 12 ) );
		processed.add( bundle.vertices.get( 13 ) );
		processed.add( bundle.vertices.get( 14 ) );
		processed.add( bundle.vertices.get( 15 ) );

		processed.add( bundle.vertices.get( 4 ) );
		processed.add( bundle.vertices.get( 5 ) );
		processed.add( bundle.vertices.get( 6 ) );
		processed.add( bundle.vertices.get( 7 ) );

		final RefList< TestEdge > edges = RefCollections.createRefList( bundle.graph.edges() );
		edges.add( bundle.edges.get( 10 ) );
		edges.add( bundle.edges.get( 8 ) );
		edges.add( bundle.edges.get( 11 ) );
		edges.add( bundle.edges.get( 9 ) );

		edges.add( bundle.edges.get( 2 ) );
		edges.add( bundle.edges.get( 0 ) );
		edges.add( bundle.edges.get( 3 ) );
		edges.add( bundle.edges.get( 1 ) );

		edges.add( bundle.edges.get( 14 ) );
		edges.add( bundle.edges.get( 12 ) );
		edges.add( bundle.edges.get( 15 ) );
		edges.add( bundle.edges.get( 13 ) );

		edges.add( bundle.edges.get( 6 ) );
		edges.add( bundle.edges.get( 4 ) );
		edges.add( bundle.edges.get( 7 ) );
		edges.add( bundle.edges.get( 5 ) );

		// Specifies leaf order.
		final RefList< TestVertex > leaves = RefCollections.createRefList( bundle.graph.vertices(), 4 );
		leaves.add( bundle.vertices.get( 11 ) );
		leaves.add( bundle.vertices.get( 3 ) );
		leaves.add( bundle.vertices.get( 15 ) );
		leaves.add( bundle.vertices.get( 7 ) );
		final TestVertex first = bundle.vertices.get( 11 );
		final DepthFirstCrossComponentSearch< TestVertex, TestEdge > bfs =
				new DepthFirstCrossComponentSearch<>( bundle.graph, SearchDirection.REVERSED, leaves );

		final TraversalTester< TestVertex, TestEdge, DepthFirstCrossComponentSearch< TestVertex, TestEdge > > traversalTester =
				new TraversalTester<>(
						expected.iterator(),
						processed.iterator(),
						edges.iterator(),
						edgeClass.iterator() );

		bfs.setTraversalListener( traversalTester );
		bfs.start( first );
		traversalTester.searchDone();
	}

	@Test
	public void testmultipleComponentsStdObjects()
	{
		final GraphTestBundle< ObjectVertex< Integer >, ObjectEdge< Integer > > bundle = GraphsForTests.multipleComponentsStdObjects();

		final List< EdgeClass > edgeClass = new ArrayList<>();
		for ( int i = 0; i < 4; i++ )
			edgeClass.addAll( Arrays.asList( new EdgeClass[] { TREE, TREE, TREE, CROSS } ) );

		// Expected iteration order.
		final RefList< ObjectVertex< Integer > > expected = RefCollections.createRefList( bundle.graph.vertices() );
		expected.add( bundle.vertices.get( 11 ) );
		expected.add( bundle.vertices.get( 9 ) );
		expected.add( bundle.vertices.get( 8 ) );
		expected.add( bundle.vertices.get( 10 ) );

		expected.add( bundle.vertices.get( 3 ) );
		expected.add( bundle.vertices.get( 1 ) );
		expected.add( bundle.vertices.get( 0 ) );
		expected.add( bundle.vertices.get( 2 ) );

		expected.add( bundle.vertices.get( 15 ) );
		expected.add( bundle.vertices.get( 13 ) );
		expected.add( bundle.vertices.get( 12 ) );
		expected.add( bundle.vertices.get( 14 ) );

		expected.add( bundle.vertices.get( 7 ) );
		expected.add( bundle.vertices.get( 5 ) );
		expected.add( bundle.vertices.get( 4 ) );
		expected.add( bundle.vertices.get( 6 ) );

		// Expected processed order.
		final RefList< ObjectVertex< Integer > > processed = RefCollections.createRefList( bundle.graph.vertices() );
		processed.add( bundle.vertices.get( 8 ) );
		processed.add( bundle.vertices.get( 9 ) );
		processed.add( bundle.vertices.get( 10 ) );
		processed.add( bundle.vertices.get( 11 ) );

		processed.add( bundle.vertices.get( 0 ) );
		processed.add( bundle.vertices.get( 1 ) );
		processed.add( bundle.vertices.get( 2 ) );
		processed.add( bundle.vertices.get( 3 ) );

		processed.add( bundle.vertices.get( 12 ) );
		processed.add( bundle.vertices.get( 13 ) );
		processed.add( bundle.vertices.get( 14 ) );
		processed.add( bundle.vertices.get( 15 ) );

		processed.add( bundle.vertices.get( 4 ) );
		processed.add( bundle.vertices.get( 5 ) );
		processed.add( bundle.vertices.get( 6 ) );
		processed.add( bundle.vertices.get( 7 ) );

		final RefList< ObjectEdge< Integer > > edges = RefCollections.createRefList( bundle.graph.edges() );
		edges.add( bundle.edges.get( 10 ) );
		edges.add( bundle.edges.get( 8 ) );
		edges.add( bundle.edges.get( 11 ) );
		edges.add( bundle.edges.get( 9 ) );

		edges.add( bundle.edges.get( 2 ) );
		edges.add( bundle.edges.get( 0 ) );
		edges.add( bundle.edges.get( 3 ) );
		edges.add( bundle.edges.get( 1 ) );

		edges.add( bundle.edges.get( 14 ) );
		edges.add( bundle.edges.get( 12 ) );
		edges.add( bundle.edges.get( 15 ) );
		edges.add( bundle.edges.get( 13 ) );

		edges.add( bundle.edges.get( 6 ) );
		edges.add( bundle.edges.get( 4 ) );
		edges.add( bundle.edges.get( 7 ) );
		edges.add( bundle.edges.get( 5 ) );

		// Specifies leaf order.
		final RefList< ObjectVertex< Integer > > leaves = RefCollections.createRefList( bundle.graph.vertices(), 4 );
		leaves.add( bundle.vertices.get( 11 ) );
		leaves.add( bundle.vertices.get( 3 ) );
		leaves.add( bundle.vertices.get( 15 ) );
		leaves.add( bundle.vertices.get( 7 ) );
		final ObjectVertex< Integer > first = bundle.vertices.get( 11 );
		final DepthFirstCrossComponentSearch< ObjectVertex< Integer >, ObjectEdge< Integer > > bfs =
				new DepthFirstCrossComponentSearch<>( bundle.graph, SearchDirection.REVERSED, leaves );

		final TraversalTester< ObjectVertex< Integer >, ObjectEdge< Integer >, DepthFirstCrossComponentSearch< ObjectVertex< Integer >, ObjectEdge< Integer > > > traversalTester =
				new TraversalTester<>(
						expected.iterator(),
						processed.iterator(),
						edges.iterator(),
						edgeClass.iterator() );

		bfs.setTraversalListener( traversalTester );
		bfs.start( first );
		traversalTester.searchDone();
	}

}
