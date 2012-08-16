public interface SequenceCacheItem extends Comparable
{
  // Get the next highest element, for end-inclusive queries.
  public SequenceCacheItem successor();
}
