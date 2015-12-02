package it.disco.unimib.stan.unit;

import it.disco.unimib.stan.core.IndexesPath;
import it.disco.unimib.stan.core.TrainingDataPath;
import it.disco.unimib.stan.core.WorkingAreaPaths;
import it.disco.unimib.stan.experiments.DataPaths;
import it.disco.unimib.stan.experiments.EvaluationPaths;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class AnnotationArea {

	public void clear() {
		FileUtils.deleteQuietly(new File(new IndexesPath().test().path()));
		FileUtils.deleteQuietly(new File(new IndexesPath().numericTest().path()));
		FileUtils.deleteQuietly(new File(new IndexesPath().labellingTest().path()));
		FileUtils.deleteQuietly(new File(new TrainingDataPath().test().path()));
		FileUtils.deleteQuietly(new File(new DataPaths().test().path()));
		FileUtils.deleteQuietly(new File(new WorkingAreaPaths().path()));
		FileUtils.deleteQuietly(new File("./tmp"));
		FileUtils.deleteQuietly(new File("./environment"));
		FileUtils.deleteQuietly(new File(new EvaluationPaths().folder("test").path()));
	}

}
