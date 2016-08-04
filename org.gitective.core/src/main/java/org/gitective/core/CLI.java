package org.gitective.core;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.gitective.core.filter.commit.DiffFileCountFilter;
import org.gitective.core.stat.CommitHistogram;
import org.gitective.core.stat.CommitHistogramFilter;
import org.gitective.core.stat.CommitterHistogramFilter;
import org.gitective.core.stat.UserCommitActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CLI {

	private static final Logger LOG = LoggerFactory.getLogger(CLI.class);

	public static void main(String[] args) throws IOException {

		Repository repo = FileRepositoryBuilder.create(new File("/home/andreas/git/ag-openhab2/.git"));
		Collection<String> branches = RepositoryUtils.getBranches(repo);

		LOG.debug("{}", branches);

		CommitFinder finder = new CommitFinder(repo);
		CommitHistogramFilter filter = new CommitterHistogramFilter();
		finder.setFilter(filter).find();

		CommitHistogram histogram = filter.getHistogram();
		UserCommitActivity[] userActivity = histogram.getUserActivity();
		
		for (UserCommitActivity userCommitActivity : userActivity) {
			LOG.debug("User: {} commits: {}", userCommitActivity.getEmail(), userCommitActivity.getCount());
		}
		
		LOG.debug("{}", histogram.getActivity("andreas.gebauer.berlin@gmail.com").getCount());
		
		
		DiffFileCountFilter diffFileCountFilter = new DiffFileCountFilter();
		finder.setFilter(diffFileCountFilter).find();

		LOG.debug("Added lines: {}",diffFileCountFilter.getAdded());
		LOG.debug("Edited lines: {}",diffFileCountFilter.getEdited());
		LOG.debug("Copied lines: {}",diffFileCountFilter.getCopied());
		LOG.debug("Deleted lines: {}",diffFileCountFilter.getDeleted());
		LOG.debug("Renamed lines: {}",diffFileCountFilter.getRenamed());
		LOG.debug("Total lines: {}",diffFileCountFilter.getTotal());
	}
}
