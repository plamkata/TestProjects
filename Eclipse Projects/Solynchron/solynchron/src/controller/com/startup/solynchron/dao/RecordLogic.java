/**
 * 
 */
package com.startup.solynchron.dao;

import java.util.List;

import javax.persistence.Query;

import com.startup.solynchron.Activator;
import com.startup.solynchron.InitializationProgressException;
import com.startup.solynchron.obj.IModelObject;
import com.startup.solynchron.obj.Record;

/**
 * @author plamKaTa
 *
 */
public class RecordLogic implements ILogic {

	/**
	 * 
	 */
	public RecordLogic() {
	}

	/* (non-Javadoc)
	 * @see com.startup.solynchron.dao.ILogic#find(java.lang.Long)
	 */
	@Override
	public IModelObject find(Long id) throws InitializationProgressException {
		if (id == null) {
			return new Record();
		} else {
			return Activator.getSession().find(Record.class, id);
		}
	}
	
	/**
	 * This is just a first test method.
	 * 
	 * TODO: remove the whole class
	 * 
	 * @return
	 * @throws InitializationProgressException
	 */
	public Long incrementRecord() throws InitializationProgressException {
		int quantity = 1;
		if (!hasDualQuantity()) {
			ProblemLogic logic = (ProblemLogic) LogicManager.get(LogicManager.PROBLEM_LOGIC);
			logic.indexAllProblems();
			quantity = 2;
		}
		
		Record r = new Record();
		r.setQuantity(quantity);
		Activator.getSession().persist(r);
		
		Query q = Activator.getSession().createQuery(
		"select sum(r.quantity) from Record r");
		return (Long) q.getSingleResult();
	}
	
	public boolean hasDualQuantity() throws InitializationProgressException {
		Query q = Activator.getSession().createQuery(
			"from Record r where r.quantity = 2");
		List<Record> records = q.getResultList();
		return !records.isEmpty();
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}