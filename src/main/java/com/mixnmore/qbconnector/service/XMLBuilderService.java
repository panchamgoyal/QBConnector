package com.mixnmore.qbconnector.service;

import com.mixnmore.qbconnector.types.Term;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 * @author Pranshu Agarwal
 */
public class XMLBuilderService {

	private static final Logger LOG = Logger.getLogger(XMLBuilderService.class.getName());

	public String buildTermsAddXml() {
		List<Term> terms = DataConnector.getNewTerms();
		String bodyXml = "";
		if (terms == null || terms.isEmpty()) {
			return bodyXml;
		}
		try {
			for (Term term : terms) {
				String xml = IOUtils.resourceToString("templates\\TermsAdd.xml", null, getClass().getClassLoader());
				xml = xml.replace("##TERMNAME##", term.name == null ? "" : term.name);
				xml = xml.replace("##ACTIVE##", term.active ? "true" : "false");
				xml = xml.replace("##DUEDAYS##", Integer.toString(term.dueAfterDays));
				bodyXml += xml;
			}
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "Unable to load XML message template file", ex);
			return null;
		}
		return insertIntoBaseXml(bodyXml);
	}
	
	private String insertIntoBaseXml(String body) {
		try {
			String baseXml = IOUtils.resourceToString("templates\\Base.xml", null, getClass().getClassLoader());
			baseXml = baseXml.replace("##REQUESTBODY##", body);
			return baseXml;
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "Unable to load base template file", ex);
			return "";
		}
	}
}
