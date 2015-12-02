package org.getopt.luke;

import java.io.IOException;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.*;

public class IntervalLimitedCollector extends LimitedHitCollector {
  private long maxTime;
  private long lastDoc = 0;
  private TopScoreDocCollector tdc;
  private TopDocs topDocs = null;
  private TimeLimitingCollector thc;
  
  
  public IntervalLimitedCollector(int maxTime) {
    this.maxTime = maxTime;
    tdc = TopScoreDocCollector.create(1000);
    thc = new TimeLimitingCollector(tdc, TimeLimitingCollector.getGlobalCounter(), maxTime);
  }

  /* (non-Javadoc)
   * @see org.getopt.luke.LimitedHitCollector#limitSize()
   */
  @Override
  public long limitSize() {
    return maxTime;
  }

  /* (non-Javadoc)
   * @see org.getopt.luke.LimitedHitCollector#limitType()
   */
  @Override
  public int limitType() {
    return TYPE_TIME;
  }

  @Override
  public int getDocId(int pos) {
    if (topDocs == null) {
      topDocs = tdc.topDocs();
    }
    return topDocs.scoreDocs[pos].doc;
  }

  /* (non-Javadoc)
   * @see org.getopt.luke.AccessibleHitCollector#getScore(int)
   */
  @Override
  public float getScore(int pos) {
    if (topDocs == null) {
      topDocs = tdc.topDocs();
    }
    return topDocs.scoreDocs[pos].score;
  }

  @Override
  public int getTotalHits() {
    return tdc.getTotalHits();
  }

  @Override
  public void reset() {
    lastDoc = 0;
    tdc = TopScoreDocCollector.create(1000);
    thc = new TimeLimitingCollector(tdc, TimeLimitingCollector.getGlobalCounter(), maxTime);
  }

  @Override
  public LeafCollector getLeafCollector(LeafReaderContext leafReaderContext) throws IOException {
    return thc.getLeafCollector(leafReaderContext);
  }

  @Override
  public boolean needsScores() {
    return thc.needsScores();
  }
}
