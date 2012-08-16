public enum EnumDisplay
{
   MR("mister"),
   MRS("misses"),
   MS("mizz");

   private final String displayValue;

   EnumDisplay( String disp )
   {
     this.displayValue = disp;
   }

   public String displayValue()
   {
      return this.displayValue;
   }

}
