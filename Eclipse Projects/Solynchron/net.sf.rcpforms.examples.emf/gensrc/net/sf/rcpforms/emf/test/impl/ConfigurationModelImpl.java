/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.sf.rcpforms.emf.test.impl;

import java.util.Collection;

import net.sf.rcpforms.emf.test.ConfigurationModel;
import net.sf.rcpforms.emf.test.TestModel;
import net.sf.rcpforms.emf.test.TestPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Configuration Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.sf.rcpforms.emf.test.impl.ConfigurationModelImpl#getTestModels <em>Test Models</em>}</li>
 *   <li>{@link net.sf.rcpforms.emf.test.impl.ConfigurationModelImpl#getTestModel <em>Test Model</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConfigurationModelImpl extends EObjectImpl implements ConfigurationModel {
	/**
	 * The cached value of the '{@link #getTestModels() <em>Test Models</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTestModels()
	 * @generated
	 * @ordered
	 */
	protected EList<TestModel> testModels;

	/**
	 * The cached value of the '{@link #getTestModel() <em>Test Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTestModel()
	 * @generated
	 * @ordered
	 */
	protected TestModel testModel;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConfigurationModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestPackage.Literals.CONFIGURATION_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TestModel> getTestModels() {
		if (testModels == null) {
			testModels = new EObjectContainmentEList<TestModel>(TestModel.class, this, TestPackage.CONFIGURATION_MODEL__TEST_MODELS);
		}
		return testModels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TestModel getTestModel() {
		return testModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTestModel(TestModel newTestModel, NotificationChain msgs) {
		TestModel oldTestModel = testModel;
		testModel = newTestModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TestPackage.CONFIGURATION_MODEL__TEST_MODEL, oldTestModel, newTestModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTestModel(TestModel newTestModel) {
		if (newTestModel != testModel) {
			NotificationChain msgs = null;
			if (testModel != null)
				msgs = ((InternalEObject)testModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TestPackage.CONFIGURATION_MODEL__TEST_MODEL, null, msgs);
			if (newTestModel != null)
				msgs = ((InternalEObject)newTestModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TestPackage.CONFIGURATION_MODEL__TEST_MODEL, null, msgs);
			msgs = basicSetTestModel(newTestModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.CONFIGURATION_MODEL__TEST_MODEL, newTestModel, newTestModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TestPackage.CONFIGURATION_MODEL__TEST_MODELS:
				return ((InternalEList<?>)getTestModels()).basicRemove(otherEnd, msgs);
			case TestPackage.CONFIGURATION_MODEL__TEST_MODEL:
				return basicSetTestModel(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TestPackage.CONFIGURATION_MODEL__TEST_MODELS:
				return getTestModels();
			case TestPackage.CONFIGURATION_MODEL__TEST_MODEL:
				return getTestModel();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case TestPackage.CONFIGURATION_MODEL__TEST_MODELS:
				getTestModels().clear();
				getTestModels().addAll((Collection<? extends TestModel>)newValue);
				return;
			case TestPackage.CONFIGURATION_MODEL__TEST_MODEL:
				setTestModel((TestModel)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case TestPackage.CONFIGURATION_MODEL__TEST_MODELS:
				getTestModels().clear();
				return;
			case TestPackage.CONFIGURATION_MODEL__TEST_MODEL:
				setTestModel((TestModel)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case TestPackage.CONFIGURATION_MODEL__TEST_MODELS:
				return testModels != null && !testModels.isEmpty();
			case TestPackage.CONFIGURATION_MODEL__TEST_MODEL:
				return testModel != null;
		}
		return super.eIsSet(featureID);
	}

} //ConfigurationModelImpl
