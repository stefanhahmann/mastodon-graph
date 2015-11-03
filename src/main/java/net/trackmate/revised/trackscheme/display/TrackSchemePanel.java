package net.trackmate.revised.trackscheme.display;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JPanel;

import net.imglib2.ui.InteractiveDisplayCanvasComponent;
import net.imglib2.ui.OverlayRenderer;
import net.imglib2.ui.PainterThread;
import net.imglib2.ui.TransformListener;
import net.trackmate.revised.trackscheme.LineageTreeLayout;
import net.trackmate.revised.trackscheme.ScreenEntities;
import net.trackmate.revised.trackscheme.ScreenEntitiesInterpolator;
import net.trackmate.revised.trackscheme.ScreenTransform;
import net.trackmate.revised.trackscheme.TrackSchemeGraph;
import net.trackmate.revised.trackscheme.TrackSchemeHighlight;
import net.trackmate.revised.trackscheme.display.TrackSchemeOptions.Values;
import net.trackmate.revised.trackscheme.display.laf.DefaultTrackSchemeOverlay;
import net.trackmate.revised.ui.selection.HighlightListener;
import net.trackmate.trackscheme.animate.AbstractAnimator;

public class TrackSchemePanel extends JPanel implements TransformListener< ScreenTransform >, PainterThread.Paintable, HighlightListener
{
	private final TrackSchemeGraph< ?, ? > graph;

	/**
	 * trackscheme options.
	 */
	private final Values options;

	/**
	 * Canvas used for displaying the trackscheme graph.
	 */
	private final InteractiveDisplayCanvasComponent< ScreenTransform > display;

	/**
	 * The current transform from layout to screen coordinates.
	 */
	private final ScreenTransform screenTransform;

	/**
	 * layout the {@link TrackSchemeGraph} into layout coordinates.
	 */
	private final LineageTreeLayout layout;

	/**
	 * compute {@link ScreenEntities} from {@link LineageTreeLayout} using the
	 * current {@link ScreenTransform} and interpolate between
	 * {@link ScreenEntities} for animation.
	 */
	private final ScreenEntityAnimator entityAnimator;

	private final PainterThread painterThread;

	private final AbstractTrackSchemeOverlay graphOverlay;

	// TODO rename
	private final Flags flags;

	public TrackSchemePanel(
			final TrackSchemeGraph< ?, ? > graph,
			final TrackSchemeHighlight< ?, ? > highlight,
			final TrackSchemeOptions optional )
	{
		super( new BorderLayout(), false );
		this.graph = graph;
		options = optional.values;

		final int w = options.getWidth();
		final int h = options.getHeight();
		display = new InteractiveDisplayCanvasComponent< ScreenTransform >(	w, h, options.getTransformEventHandlerFactory() );
		display.addTransformListener( this );

		graphOverlay = new DefaultTrackSchemeOverlay( highlight, optional );
		highlight.addHighlightListener( this );
		display.addOverlayRenderer( graphOverlay );

		// This should be the last OverlayRenderer in display.
		// It triggers repainting if there is currently an ongoing animation.
		display.addOverlayRenderer( new OverlayRenderer()
		{
			@Override
			public void setCanvasSize( final int width, final int height )
			{}

			@Override
			public void drawOverlays( final Graphics g )
			{
				checkAnimate();
			}
		} );

		screenTransform = new ScreenTransform();
		layout = new LineageTreeLayout( graph );
		entityAnimator = new ScreenEntityAnimator();
		painterThread = new PainterThread( this );
		flags = new Flags();

		display.addMouseMotionListener( new MouseOverListener( graphOverlay ) );

		add( display, BorderLayout.CENTER );
		painterThread.start();
	}

	/**
	 * Stop the {@link #painterThread}.
	 */
	public void stop()
	{
		painterThread.interrupt();
	}

	/**
	 * request repainting if there is currently an ongoing animation.
	 */
	void checkAnimate()
	{
		if ( !entityAnimator.isComplete() )
			painterThread.requestRepaint();
	}

	@Override
	public void paint()
	{
		final Flags flags = this.flags.clear();
		if ( flags.graphChanged )
		{
			layout.layout();
			entityAnimator.startAnimation( screenTransform, 0 );
		}
		else if ( flags.transformChanged )
		{
			entityAnimator.startAnimation( screenTransform, 0 );
		}
		else if ( flags.contextChanged )
		{
			System.out.println( "if ( flags.contextChanged ): NOT IMPLEMENTED ");
		}

		entityAnimator.setTime( System.currentTimeMillis() );
		entityAnimator.setPaintEntities( graphOverlay );
		display.repaint();
	}

	@Override
	public void transformChanged( final ScreenTransform transform )
	{
		synchronized( screenTransform )
		{
			screenTransform.set( transform );
		}
		flags.setTransformChanged();
		painterThread.requestRepaint();
	}

//	@Override // TODO: should be some listener interface. rename?
	public void graphChanged()
	{
		flags.setGraphChanged();
		painterThread.requestRepaint();
	}

	@Override
	public void highlightChanged()
	{
		display.repaint();
	}

	protected class ScreenEntityAnimator extends AbstractAnimator
	{
		private ScreenEntities screenEntities;

		private ScreenEntities screenEntities2;

		private ScreenEntities screenEntitiesIpStart;

		private ScreenEntities screenEntitiesIpEnd;

		private ScreenEntitiesInterpolator interpolator;

		private final int capacity = 1000;

		public ScreenEntityAnimator()
		{
			super( 0 );
			screenEntities = new ScreenEntities( graph, capacity );
			screenEntities2 = new ScreenEntities( graph, capacity );
			screenEntitiesIpStart = new ScreenEntities( graph, capacity );
			screenEntitiesIpEnd = new ScreenEntities( graph, capacity );
			interpolator = null;
		}

		/**
		 * Swap screenEntities and screenEntities2.
		 */
		private void swapPools()
		{
			final ScreenEntities tmp = screenEntities;
			screenEntities = screenEntities2;
			screenEntities2 = tmp;
			screenEntities.clear();
		}

		/**
		 * Swap screenEntities and screenEntitiesIpStart.
		 */
		private void swapIpStart()
		{
			final ScreenEntities tmp = screenEntities;
			screenEntities = screenEntitiesIpStart;
			screenEntitiesIpStart = tmp;
			screenEntities.clear();
		}

		/**
		 * Swap screenEntities and screenEntitiesIpEnd.
		 */
		private void swapIpEnd()
		{
			final ScreenEntities tmp = screenEntities;
			screenEntities = screenEntitiesIpEnd;
			screenEntitiesIpEnd = tmp;
			screenEntities.clear();
		}

		/**
		 *
		 * @param transform
		 * @param duration animation duration (in time units), may be 0.
		 */
		public void startAnimation( final ScreenTransform transform, final long duration )
		{
			reset( duration );
			if (duration > 0 )
			{
				swapIpStart();
				layout.cropAndScale( transform, screenEntities );
				swapIpEnd();
				interpolator = new ScreenEntitiesInterpolator( screenEntitiesIpStart, screenEntitiesIpEnd );
			}
			else
			{
				interpolator = null;
				swapPools();
				layout.cropAndScale( transform, screenEntities );
			}
		}

		@Override
		public void setTime( final long time )
		{
			super.setTime( time );
			if ( interpolator != null )
			{
				swapPools();
				interpolator.interpolate( ratioComplete(), screenEntities );
				if ( isComplete() )
					interpolator = null;
			}
		}

		/**
		 * Set entities for painting into the specified double-buffered
		 * {@link AbstractTrackSchemeOverlay}. (This swaps
		 * {@link #screenEntities} with pending entities from the overlay.)
		 */
		public void setPaintEntities( final AbstractTrackSchemeOverlay overlay )
		{
			final ScreenEntities tmp = overlay.setScreenEntities( screenEntities );
			if ( tmp == null )
				screenEntities = new ScreenEntities( graph, capacity );
			else
				screenEntities = tmp;
		}
	}

	protected static class Flags
	{
		private boolean transformChanged;
		private boolean graphChanged;
		private boolean contextChanged;

		public Flags()
		{
			transformChanged = false;
			graphChanged = false;
			contextChanged = false;
		}

		public Flags( final Flags f )
		{
			transformChanged = f.transformChanged;
			graphChanged = f.graphChanged;
			contextChanged = f.contextChanged;
		}

		public synchronized void setTransformChanged()
		{
			transformChanged = true;
		}

		public synchronized void setGraphChanged()
		{
			graphChanged = true;
		}

		public synchronized void setContextChanged()
		{
			contextChanged = true;
		}

		public synchronized Flags clear()
		{
			final Flags copy = new Flags( this );
			transformChanged = false;
			graphChanged = false;
			contextChanged = false;
			return copy;
		}
	}
}
