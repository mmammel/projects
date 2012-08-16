import java.io.Serializable;
import java.util.List;

public class Slice implements Comparable, Serializable
{

    // Local Data

    /**
     * id is the id from the database, and is not used in non persisted Slice
     * objects. A -1 value indicates a transient, non persisted Slice.
     */
    final long id;
    long startTime;
    long endTime;
    long availableSeconds;
    String hostName;
    String jvmID;
    String archiveFolder;
    long archiveFile;
    boolean dirty;
    String vendor;
    double srResponseTimeSum;
    long srHitsSum;
    double heapUsedAvg;
    double cpuUsage;

    // State data
    boolean hasInvocationData = false;
    boolean hasLeakData = false;
    boolean hasActiveInvocationData = false;
    boolean hasAppServerMetricData = false;
    boolean hasMemoryData = false;

    // DAO object references
    List invocations;
    List leakData;
    List activeInvocations;
    List appServerMetrics;
    //Memory memory;

    /**
     * Public constructor used to create transient, non persistant Slice
     * objects. To make a persistant Slice create a transient Slice and use the
     * DataAccessManager to persist it.
     */
    public Slice( String hostName, String jvmID, long startTime, long endTime,  String archiveFolder) {

        // set to -1 to indicate this is a transient, non persistant Slice
        // object
        this.id = -1;
        this.startTime  = startTime;
        this.endTime = endTime;
        this.hostName = hostName;
        this.jvmID = jvmID;
        this.archiveFolder = archiveFolder;
    }
    
    /**
     * Public constructor used to create transient, non persistent Slice
     * objects. To make a persistent Slice create a transient Slice and use the
     * DataAccessManager to persist it.  The archive folder defaults to 'active'
     */
    public Slice( String hostName, String jvmID, long startTime, long endTime) {

        // set to -1 to indicate this is a transient, non persistant Slice
        // object
        this.id = -1;
        this.startTime  = startTime;
        this.endTime = endTime;
        this.hostName = hostName;
        this.jvmID = jvmID;
        this.archiveFolder = "active";
    }
    
    /**
     * This is a shallow copy.  Only the 
     * local Slice data is copied, and the id is set to -1 to indicate a transient Slice
     * @param oldSlice
     */
    public Slice createProtoSlice(){
        
        Slice protoSlice = new Slice( this.hostName, this.jvmID, this.startTime, this.endTime, this.archiveFolder);        
 
        protoSlice.archiveFolder = this.archiveFolder;        
        protoSlice.hasActiveInvocationData = this.hasActiveInvocationData;
        protoSlice.hasAppServerMetricData = this.hasAppServerMetricData;
        protoSlice.hasInvocationData = this.hasInvocationData;
        protoSlice.hasLeakData = this.hasLeakData;
        protoSlice.hasMemoryData = this.hasMemoryData;
        
        return protoSlice;
        
    }

    /**
     * Package level constructor, used by the com.precise.javaperf.db.access
     * package to create Slices from the database.
     *
     * @param id
     * @param startTime
     * @param endTime
     * @param availableSeconds
     * @param hostName
     * @param jvmID
     * @param archiveFolder
     * @param archiveFile
     * @param dirty
     * @param vendor
     * @param srResponseTimeSum
     * @param srHitsSum
     * @param heapUsedAvg
     */
    Slice(long id, long startTime, long endTime, long availableSeconds,
            String hostName, String jvmID, String archiveFolder,
            long archiveFile, boolean dirty, String vendor,
            double srResponseTimeSum, long srHitsSum, double heapUsedAvg, double cpuUsage) {

        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.availableSeconds = availableSeconds;
        this.hostName = hostName;
        this.jvmID = jvmID;
        this.archiveFolder = archiveFolder;
        this.archiveFile = archiveFile;
        this.dirty = dirty;
        this.vendor = vendor;
        this.srResponseTimeSum = srResponseTimeSum;
        this.srHitsSum = srHitsSum;
        this.heapUsedAvg = heapUsedAvg;
        this.cpuUsage = cpuUsage;

    }

    /**
     * @return the invocation
     */
    public List getInvocation() {
        return invocations;
    }

    /**
     * @param invocation
     *            the invocation to set
     */
    public void setInvocations(List invocations) {
//        if( invocations == null){
//            return;
//        }
        this.invocations = invocations;
        this.hasInvocationData = true;
    }

    /**
     * @return the archiveFile
     */
    public long getArchiveFile() {
        return archiveFile;
    }

    /**
     * @param archiveFile
     *            the archiveFile to set
     */
    public void setArchiveFile(long archiveFile) {
        this.archiveFile = archiveFile;
    }

    /**
     * @return the archiveFolder
     */
    public String getArchiveFolder() {
        return archiveFolder;
    }

    /**
     * @param archiveFolder
     *            the archiveFolder to set
     */
    public void setArchiveFolder(String archiveFolder) {
        this.archiveFolder = archiveFolder;
    }

    /**
     * @return the availableSeconds
     */
    public long getAvailableSeconds() {
        return availableSeconds;
    }

    /**
     * @param availableSeconds
     *            the availableSeconds to set
     */
    public void setAvailableSeconds(long availableSeconds) {
        this.availableSeconds = availableSeconds;
    }

    /**
     * @return the dirty
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * @param dirty
     *            the dirty to set
     */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /**
     * @return the endTime
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     *            the endTime to set
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the heapUsedAvg
     */
    public double getHeapUsedAvg() {
        return heapUsedAvg;
    }

    /**
     * @param heapUsedAvg
     *            the heapUsedAvg to set
     */
    public void setHeapUsedAvg(double heapUsedAvg) {
        this.heapUsedAvg = heapUsedAvg;
    }

    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName
     *            the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the jvmID
     */
    public String getJvmID() {
        return jvmID;
    }

    /**
     * @param jvmID
     *            the jvmID to set
     */
    public void setJvmID(String jvmID) {
        this.jvmID = jvmID;
    }

    /**
     * @return the srHitsSum
     */
    public long getSrHitsSum() {
        return srHitsSum;
    }

    /**
     * @param srHitsSum
     *            the srHitsSum to set
     */
    public void setSrHitsSum(long srHitsSum) {
        this.srHitsSum = srHitsSum;
    }

    /**
     * @return the srResponseTimeSum
     */
    public double getSrResponseTimeSum() {
        return srResponseTimeSum;
    }

    /**
     * @param srResponseTimeSum
     *            the srResponseTimeSum to set
     */
    public void setSrResponseTimeSum(double srResponseTimeSum) {
        this.srResponseTimeSum = srResponseTimeSum;
    }

    /**
     * @return the startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the vendor
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * @param vendor
     *            the vendor to set
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    /**
     * @return the hasActiveInvocationData
     */
    public boolean hasActiveInvocationData() {
        return hasActiveInvocationData;
    }

    /**
     * @param hasActiveInvocationData the hasActiveInvocationData to set
     */
    public void setHasActiveInvocationData(boolean hasActiveInvocationData) {
        this.hasActiveInvocationData = hasActiveInvocationData;
    }

    /**
     * @return the hasAppServerMetricData
     */
    public boolean hasAppServerMetricData() {
        return hasAppServerMetricData;
    }

    /**
     * @param hasAppServerMetricData the hasAppServerMetricData to set
     */
    public void setHasAppServerMetricData(boolean hasAppServerMetricData) {
        this.hasAppServerMetricData = hasAppServerMetricData;
    }

    /**
     * @return the hasInvocationData
     */
    public boolean hasInvocationData() {
        return hasInvocationData;
    }

    /**
     * @param hasInvocationData the hasInvocationData to set
     */
    public void setHasInvocationData(boolean hasInvocationData) {
        this.hasInvocationData = hasInvocationData;
    }

    /**
     * @return the hasLeakData
     */
    public boolean hasLeakData() {
        return hasLeakData;
    }

    /**
     * @param hasLeakData the hasLeakData to set
     */
    public void setHasLeakData(boolean hasLeakData) {
        this.hasLeakData = hasLeakData;
    }

    /**
     * @return the hasMemoryData
     */
    public boolean hasMemoryData() {
        return hasMemoryData;
    }

    /**
     * @param hasMemoryData the hasMemoryData to set
     */
    public void setHasMemoryData(boolean hasMemoryData) {
        this.hasMemoryData = hasMemoryData;
    }

    /**
     * @return the activeInvocation
     */
    public List getActiveInvocations() {
        return activeInvocations;
    }

    /**
     * @param activeInvocation the activeInvocation to set
     */
    public void setActiveInvocations(List activeInvocations) {
        this.hasActiveInvocationData = true;
        this.activeInvocations = activeInvocations;
    }

    /**
     * @return the appServerMetrics
     */
    public List getAppServerMetrics() {
        return appServerMetrics;
    }

    /**
     * @param appServerMetrics the appServerMetrics to set
     */
    public void setAppServerMetrics(List appServerMetrics) {
        this.hasAppServerMetricData = true;
        this.appServerMetrics = appServerMetrics;
    }

    /**
     * @return the leakData
     */
    public List getLeakData() {
        return leakData;
    }

    /**
     * @param leakData the leakData to set
     */
    public void setLeakData(List leakData) {
        this.hasLeakData = true;
        this.leakData = leakData;
    }

    /**
     * @return the memory

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.hasMemoryData = true;
        this.memory = memory;
    }
*/
    /**
     * @return the cpuUsage
     */
    public double getCpuUsage() {
        return cpuUsage;
    }

    /**
     * @param cpuUsage the cpuUsage to set
     */
    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public boolean isTransient()
    {
      return this.id == -1;
    }
    
    public int compareTo(Object aThat) {              
        
        Slice that = (Slice) aThat;
        
//        if( this.id ==  that.id ){
//            return 0;
//        }
        
        if( this.hostName.compareTo(that.hostName) != 0){
            return  this.hostName.compareTo(that.hostName);
        }
        if( this.jvmID.compareTo(that.jvmID) != 0){
            return this.jvmID.compareTo(that.jvmID);
        }
        
        return new Long(this.endTime).compareTo(new Long( that.endTime));
    }
    
    
    
    /* Only applicable to persistant Slices
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals( Object aThat){
        
        if(!(aThat instanceof Slice)){
            return false;
        }
        
        Slice that = (Slice) aThat;
        
//        if( this.id == -1 || that.id == -1){
//            return false;
//        }
//        
//        if( this.id != that.id){
//            return false;
//        }
        
        if( this.endTime == that.endTime){
            return true;
        }
        
        return false;
        
    }
    
    public String toString(){
        
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("id=" + id );
        buffer.append("\n");
        buffer.append("startTime=" + startTime );
        buffer.append("\n");
        buffer.append("endTime=" + endTime);
        buffer.append("\n");
        buffer.append("hostName=" + hostName );
        buffer.append("\n");
        buffer.append("jvmID=" + jvmID);
        buffer.append("\n");
        
        return buffer.toString();
    }

    public boolean containsTime( long time )
    {
      return (this.startTime <= time && this.endTime >= time);
    }
}
