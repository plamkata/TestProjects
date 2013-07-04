/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.sf.rcpforms.emf.test;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Configuration Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.rcpforms.emf.test.ConfigurationModel#getTestModels <em>Test Models</em>}</li>
 *   <li>{@link net.sf.rcpforms.emf.test.ConfigurationModel#getTestModel <em>Test Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.rcpforms.emf.test.TestPackage#getConfigurationModel()
 * @model
 * @generated
 */
public interface ConfigurationModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Test Models</b></em>' containment reference list.
	 * The list contents are of type {@link net.sf.rcpforms.emf.test.TestModel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Test Models</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Test Models</em>' containment reference list.
	 * @see net.sf.rcpforms.emf.test.TestPackage#getConfigurationModel_TestModels()
	 * @model containment="true"
	 * @generated
	 */
	EList<TestModel> getTestModels();

	/**
	 * Returns the value of the '<em><b>Test Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Test Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Test Model</em>' containment reference.
	 * @see #setTestModel(TestModel)
	 * @see net.sf.rcpforms.emf.test.TestPackage#getConfigurationModel_TestModel()
	 * @model containment="true" required="true"
	 * @generated
	 */
	TestModel getTestModel();

	/**
	 * Sets the value of the '{@link net.sf.rcpforms.emf.test.ConfigurationModel#getTestModel <em>Test Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Test Model</em>' containment reference.
	 * @see #getTestModel()
	 * @generated
	 */
	void setTestModel(TestModel value);

} // ConfigurationModel
