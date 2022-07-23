package fiji.plugin.trackmate.action.closegaps;

public class CloseGapsModel
{

	public final Method method;

	public final boolean logAutoRadius;

	public final double logRadius;

	public final boolean hessianAutoRadius;

	public final double hessianRadiusXY;

	public final double hessianRadiusZ;

	public final boolean selectionOnly;

	private CloseGapsModel( final Method method,
			final boolean logAutoRadius,
			final double logRadius,
			final boolean hessianAutoRadius,
			final double hessianRadiusXY,
			final double hessianRadiusZ,
			final boolean selectionOnly )
	{
		this.method = method;
		this.logAutoRadius = logAutoRadius;
		this.logRadius = logRadius;
		this.hessianAutoRadius = hessianAutoRadius;
		this.hessianRadiusXY = hessianRadiusXY;
		this.hessianRadiusZ = hessianRadiusZ;
		this.selectionOnly = selectionOnly;
	}

	public static Builder create()
	{
		return new Builder();
	}

	public static class Builder
	{

		private Method method = Method.LINEAR_INTERPOLATION;

		private boolean logAutoRadius = true;

		private double logRadius = 2.5;

		private boolean hessianAutoRadius = true;

		private double hessianRadiusXY = 2.5;

		private double hessianRadiusZ = 6.;

		private boolean selectionOnly = false;

		public Builder method( final Method method )
		{
			this.method = method;
			return this;
		}

		public Builder logAutoRadius( final boolean logAutoRadius )
		{
			this.logAutoRadius = logAutoRadius;
			return this;
		}

		public Builder logRadius( final double logRadius )
		{
			this.logRadius = logRadius;
			return this;
		}

		public Builder hessianAutoRadius( final boolean hessianAutoRadius )
		{
			this.hessianAutoRadius = hessianAutoRadius;
			return this;
		}

		public Builder hessianRadiusXY( final double hessianRadiusXY )
		{
			this.hessianRadiusXY = hessianRadiusXY;
			return this;
		}

		public Builder hessianRadiusZ( final double hessianRadiusZ )
		{
			this.hessianRadiusZ = hessianRadiusZ;
			return this;
		}

		public Builder selectionOnly( final boolean selectionOnly )
		{
			this.selectionOnly = selectionOnly;
			return this;
		}

		public CloseGapsModel get()
		{
			return new CloseGapsModel(
					method,
					logAutoRadius,
					logRadius,
					hessianAutoRadius,
					hessianRadiusXY,
					hessianRadiusZ,
					selectionOnly );
		}
	}

	public enum Method
	{
		LINEAR_INTERPOLATION,
		LOG_DETECTOR,
		HESSIAN_DETECTOR;
	}
}
