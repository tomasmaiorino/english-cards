package com.tsm.cards.controller;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.groups.Default;

import com.tsm.cards.model.BaseModel;
import com.tsm.cards.resources.IParser;
import com.tsm.cards.service.BaseService;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings(value = { "unchecked" })
@Slf4j
public abstract class RestBaseController<R, M extends BaseModel, I extends Serializable> extends BaseController {

	public abstract BaseService<M, I> getService();

	public abstract IParser<R, M> getParser();

	public R save(final R resource) {
		log.debug("Recieved a request to create a resource  [{}].", resource);

		validate(resource, Default.class);

		M model = getParser().toModel(resource);

		model = getService().save(model);

		R result = getParser().toResource(model);

		log.debug("returning resource [{}].", result);

		return result;
	}

	public R update(final Integer id, final R resource) {
		log.debug("Recieved a request to update a resource  [{}].", resource);

		validate(resource, Default.class);

		M model = getParser().toModel(resource);

		M origin = getService().findById((I) id);

		model = getService().update(origin, model);

		R result = getParser().toResource(model);

		log.debug("returning resource [{}].", result);

		return result;
	}

	public R findById(final Integer id) {

		log.info("Recieved a request to find an model by id [{}].", id);

		M model = getService().findById((I) id);

		R result = getParser().toResource(model);

		log.info("returning resource: [{}].", result);

		return result;
	}

	public void delete(final Integer id) {
		log.debug("Recieved a request to delete [{}].", id);

		M model = getService().findById((I) id);

		getService().delete(model);

		log.debug("model deleted.");
	}

	public Set<R> findAll(final String q) {

		log.debug("Recieved a request to find all models.");

		Set<M> models = getService().findAll();

		Set<R> resources = new HashSet<>();
		if (!models.isEmpty()) {
			resources = getParser().toResources(models);
		}

		log.debug("returning resources: [{}].", resources.size());

		return resources;
	}

}
