/**********************************************************************
Copyright (c) 2006 Erik Bengtson and others. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Contributors:
2008 Andy Jefferson - fixed transitionReadField, transitionWriteField exceptions
    ...
**********************************************************************/
package org.datanucleus.api.jpa.state;

import org.datanucleus.FetchPlan;
import org.datanucleus.Transaction;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.state.IllegalStateTransitionException;
import org.datanucleus.state.LifeCycleState;
import org.datanucleus.state.ObjectProvider;
import org.datanucleus.util.Localiser;

/**
 * Class representing the life cycle state of PersistentNontransactional.
 */
class PersistentNontransactional extends LifeCycleState
{
    /** Protected Constructor to prevent external instantiation. */
    protected PersistentNontransactional()
    {
		isPersistent = true;    	
        isDirty = false;
        isNew = false;
        isDeleted = false;
        isTransactional = false;

        stateType = P_NONTRANS;
    }

    /**
     * Method to transition to delete persistent.
     * @param op ObjectProvider.
     * @return new LifeCycle state.
     **/
    public LifeCycleState transitionDeletePersistent(ObjectProvider op)
    {
        return changeState(op, P_DELETED);
    }

    /**
     * Method to transition to transactional.
     * @param op ObjectProvider.
     * @param refreshFields Whether to refresh loaded fields
     * @return new LifeCycle state.
     */
    public LifeCycleState transitionMakeTransactional(ObjectProvider op, boolean refreshFields)
    {
        if (refreshFields)
        {
            op.refreshLoadedFields();
        }
        return changeState(op, P_CLEAN);
    }

    /**
     * Method to transition to transient.
     * @param op ObjectProvider.
     * @param useFetchPlan to make transient the fields in the fetch plan
     * @return new LifeCycle state.
     **/
    public LifeCycleState transitionMakeTransient(ObjectProvider op, boolean useFetchPlan, boolean detachAllOnCommit)
    {
        if (useFetchPlan)
        {
            op.loadUnloadedFieldsInFetchPlan();
        }
        return changeState(op, TRANSIENT);
    }

    /**
     * Method to transition to commit state.
     * @param op ObjectProvider.
     * @param tx the Transaction been committed.
     * @return new LifeCycle state.
     **/
    public LifeCycleState transitionCommit(ObjectProvider op, Transaction tx)
    {
        throw new IllegalStateTransitionException(this, "commit", op);
    }

    /**
     * Method to transition to rollback state.
     * @param op ObjectProvider.
     * @param tx The transaction
     * @return new LifeCycle state.
     **/
    public LifeCycleState transitionRollback(ObjectProvider op, Transaction tx)
    {
        throw new IllegalStateTransitionException(this, "rollback", op);
    }

    /**
     * Method to transition to refresh state.
     * @param op ObjectProvider.
     * @return new LifeCycle state.
     **/
    public LifeCycleState transitionRefresh(ObjectProvider op)
    {
        // Refresh the FetchPlan fields and unload all others
        op.refreshFieldsInFetchPlan();
        op.unloadNonFetchPlanFields();

        return this;
    }
    
    /**
     * Method to transition to evict state.
     * @param op ObjectProvider.
     * @return new LifeCycle state.
     */
    public LifeCycleState transitionEvict(ObjectProvider op)
    {
        op.clearNonPrimaryKeyFields();
        op.clearSavedFields();
        return changeState(op, HOLLOW);
    }

    /**
     * Method to transition to read-field state.
     * @param op ObjectProvider.
     * @param isLoaded if the field was previously loaded.
     * @return new LifeCycle state.
     */
    public LifeCycleState transitionReadField(ObjectProvider op, boolean isLoaded)
    {
        Transaction tx = op.getExecutionContext().getTransaction();
		if (!tx.isActive() && !tx.getNontransactionalRead())
		{
	        throw new NucleusUserException(Localiser.msg("027002"),op.getInternalObjectId());
		}

        if (tx.isActive() && ! tx.getOptimistic())
        {
            // Save the fields for rollback.
            op.saveFields();
            op.refreshLoadedFields();
            return changeState(op, P_CLEAN);
        }
        else
        {
            return this;
        }
    }

    /**
     * Method to transition to write-field state.
     * @param op ObjectProvider.
     * @return new LifeCycle state.
     **/
    public LifeCycleState transitionWriteField(ObjectProvider op)
    {
        Transaction tx = op.getExecutionContext().getTransaction();
        if (!tx.isActive() && !tx.getNontransactionalWrite())
        {
            throw new NucleusUserException(Localiser.msg("027001"), op.getInternalObjectId());
        }
        if (tx.isActive())
        {
            // Save the fields for rollback.
            op.saveFields();

            return changeState(op, P_DIRTY);
        }
        else
        {
            return this;
        }
    }

    /**
     * Method to transition to retrieve state.
     * @param op ObjectProvider.
	 * @param fgOnly only the current fetch group fields
     * @return new LifeCycle state.
     **/
    public LifeCycleState transitionRetrieve(ObjectProvider op, boolean fgOnly)
    {
        Transaction tx = op.getExecutionContext().getTransaction();
        if (tx.isActive() && !tx.getOptimistic())
        {
            // Save the fields for rollback.
            op.saveFields();
    		if (fgOnly)
            {
                op.loadUnloadedFieldsInFetchPlan();
            }
    		else
            {
    			op.loadUnloadedFields();
            }             
            return changeState(op, P_CLEAN);
        }
        else if (tx.isActive() && tx.getOptimistic())
        {
            // Save the fields for rollback.
            op.saveFields();
    		if (fgOnly)
            {
                op.loadUnloadedFieldsInFetchPlan();
            }
    		else
            {
    			op.loadUnloadedFields();
            }
    		return this;
        }
        else
        {
    		if (fgOnly)
            {
                op.loadUnloadedFieldsInFetchPlan();
            }
    		else
            {
    			op.loadUnloadedFields();
            }
    		return this;
        }
    }

    /**
     * Method to transition to retrieve state.
     * @param op ObjectProvider.
     * @param fetchPlan the fetch plan to load fields
     * @return new LifeCycle state.
     **/
    public LifeCycleState transitionRetrieve(ObjectProvider op, FetchPlan fetchPlan)
    {
        Transaction tx = op.getExecutionContext().getTransaction();
        if (tx.isActive() && !tx.getOptimistic())
        {
            // Save the fields for rollback.
            op.saveFields();
            op.loadUnloadedFieldsOfClassInFetchPlan(fetchPlan);
            return changeState(op, P_CLEAN);
        }
        else if (tx.isActive() && tx.getOptimistic())
        {
            // Save the fields for rollback.
            op.saveFields();
            op.loadUnloadedFieldsOfClassInFetchPlan(fetchPlan);
            return this;
        }
        else
        {
            op.loadUnloadedFieldsOfClassInFetchPlan(fetchPlan);
            return this;
        }
    }

    /**
     * Method to transition when serialised.
     * @param op ObjectProvider
     * @return The new LifeCycle state
     */
    public LifeCycleState transitionSerialize(ObjectProvider op)
    {
        Transaction tx = op.getExecutionContext().getTransaction();
        if (tx.isActive() && !tx.getOptimistic())
        {
            return changeState(op, P_CLEAN);
        }
        return this;
    }

    /**
     * Method to transition to detached-clean.
     * @param op ObjectProvider.
     * @return new LifeCycle state.
     */
    public LifeCycleState transitionDetach(ObjectProvider op)
    {
        return changeState(op, DETACHED_CLEAN);
    }

    /**
     * Method to return a string version of this object.
     * @return The string "P_NONTRANS".
     **/
    public String toString()
    {
        return "P_NONTRANS";
    }
}