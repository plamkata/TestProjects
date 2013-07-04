/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.sf.rcpforms.emf.test;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see net.sf.rcpforms.emf.test.TestFactory
 * @model kind="package"
 * @generated
 */
public interface TestPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "test"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://test/1.0"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "test"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TestPackage eINSTANCE = net.sf.rcpforms.emf.test.impl.TestPackageImpl.init();

	/**
	 * The meta object id for the '{@link net.sf.rcpforms.emf.test.impl.TestModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.rcpforms.emf.test.impl.TestModelImpl
	 * @see net.sf.rcpforms.emf.test.impl.TestPackageImpl#getTestModel()
	 * @generated
	 */
	int TEST_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Gender</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_MODEL__GENDER = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_MODEL__NAME = 1;

	/**
	 * The feature id for the '<em><b>Birth Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_MODEL__BIRTH_DATE = 2;

	/**
	 * The feature id for the '<em><b>Overdraw Account</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_MODEL__OVERDRAW_ACCOUNT = 3;

	/**
	 * The feature id for the '<em><b>Child Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_MODEL__CHILD_COUNT = 4;

	/**
	 * The feature id for the '<em><b>Age</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_MODEL__AGE = 5;

	/**
	 * The feature id for the '<em><b>Account Balance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_MODEL__ACCOUNT_BALANCE = 6;

	/**
	 * The feature id for the '<em><b>Is Selectable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_MODEL__IS_SELECTABLE = 7;

	/**
	 * The feature id for the '<em><b>Address</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_MODEL__ADDRESS = 8;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_MODEL_FEATURE_COUNT = 9;

	/**
	 * The meta object id for the '{@link net.sf.rcpforms.emf.test.impl.ConfigurationModelImpl <em>Configuration Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.rcpforms.emf.test.impl.ConfigurationModelImpl
	 * @see net.sf.rcpforms.emf.test.impl.TestPackageImpl#getConfigurationModel()
	 * @generated
	 */
	int CONFIGURATION_MODEL = 1;

	/**
	 * The feature id for the '<em><b>Test Models</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_MODEL__TEST_MODELS = 0;

	/**
	 * The feature id for the '<em><b>Test Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_MODEL__TEST_MODEL = 1;

	/**
	 * The number of structural features of the '<em>Configuration Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_MODEL_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.sf.rcpforms.emf.test.impl.AddressModelImpl <em>Address Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.rcpforms.emf.test.impl.AddressModelImpl
	 * @see net.sf.rcpforms.emf.test.impl.TestPackageImpl#getAddressModel()
	 * @generated
	 */
	int ADDRESS_MODEL = 2;

	/**
	 * The feature id for the '<em><b>Zip Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADDRESS_MODEL__ZIP_CODE = 0;

	/**
	 * The feature id for the '<em><b>Valid From</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADDRESS_MODEL__VALID_FROM = 1;

	/**
	 * The feature id for the '<em><b>Valid To</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADDRESS_MODEL__VALID_TO = 2;

	/**
	 * The feature id for the '<em><b>Different Post Address</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADDRESS_MODEL__DIFFERENT_POST_ADDRESS = 3;

	/**
	 * The feature id for the '<em><b>Street</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADDRESS_MODEL__STREET = 4;

	/**
	 * The feature id for the '<em><b>House Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADDRESS_MODEL__HOUSE_NUMBER = 5;

	/**
	 * The number of structural features of the '<em>Address Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADDRESS_MODEL_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link net.sf.rcpforms.emf.test.Gender <em>Gender</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.rcpforms.emf.test.Gender
	 * @see net.sf.rcpforms.emf.test.impl.TestPackageImpl#getGender()
	 * @generated
	 */
	int GENDER = 3;


	/**
	 * Returns the meta object for class '{@link net.sf.rcpforms.emf.test.TestModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see net.sf.rcpforms.emf.test.TestModel
	 * @generated
	 */
	EClass getTestModel();

	/**
	 * Returns the meta object for the attribute '{@link net.sf.rcpforms.emf.test.TestModel#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see net.sf.rcpforms.emf.test.TestModel#getName()
	 * @see #getTestModel()
	 * @generated
	 */
	EAttribute getTestModel_Name();

	/**
	 * Returns the meta object for the attribute '{@link net.sf.rcpforms.emf.test.TestModel#getBirthDate <em>Birth Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Birth Date</em>'.
	 * @see net.sf.rcpforms.emf.test.TestModel#getBirthDate()
	 * @see #getTestModel()
	 * @generated
	 */
	EAttribute getTestModel_BirthDate();

	/**
	 * Returns the meta object for the attribute '{@link net.sf.rcpforms.emf.test.TestModel#getAge <em>Age</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Age</em>'.
	 * @see net.sf.rcpforms.emf.test.TestModel#getAge()
	 * @see #getTestModel()
	 * @generated
	 */
	EAttribute getTestModel_Age();

	/**
	 * Returns the meta object for the attribute '{@link net.sf.rcpforms.emf.test.TestModel#getAccountBalance <em>Account Balance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Account Balance</em>'.
	 * @see net.sf.rcpforms.emf.test.TestModel#getAccountBalance()
	 * @see #getTestModel()
	 * @generated
	 */
	EAttribute getTestModel_AccountBalance();

	/**
	 * Returns the meta object for the attribute '{@link net.sf.rcpforms.emf.test.TestModel#getIsSelectable <em>Is Selectable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Selectable</em>'.
	 * @see net.sf.rcpforms.emf.test.TestModel#getIsSelectable()
	 * @see #getTestModel()
	 * @generated
	 */
	EAttribute getTestModel_IsSelectable();

	/**
	 * Returns the meta object for the containment reference '{@link net.sf.rcpforms.emf.test.TestModel#getAddress <em>Address</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Address</em>'.
	 * @see net.sf.rcpforms.emf.test.TestModel#getAddress()
	 * @see #getTestModel()
	 * @generated
	 */
	EReference getTestModel_Address();

	/**
	 * Returns the meta object for the attribute '{@link net.sf.rcpforms.emf.test.TestModel#getOverdrawAccount <em>Overdraw Account</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Overdraw Account</em>'.
	 * @see net.sf.rcpforms.emf.test.TestModel#getOverdrawAccount()
	 * @see #getTestModel()
	 * @generated
	 */
	EAttribute getTestModel_OverdrawAccount();

	/**
	 * Returns the meta object for the attribute '{@link net.sf.rcpforms.emf.test.TestModel#getChildCount <em>Child Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Child Count</em>'.
	 * @see net.sf.rcpforms.emf.test.TestModel#getChildCount()
	 * @see #getTestModel()
	 * @generated
	 */
	EAttribute getTestModel_ChildCount();

	/**
	 * Returns the meta object for the attribute '{@link net.sf.rcpforms.emf.test.TestModel#getGender <em>Gender</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Gender</em>'.
	 * @see net.sf.rcpforms.emf.test.TestModel#getGender()
	 * @see #getTestModel()
	 * @generated
	 */
	EAttribute getTestModel_Gender();

	/**
	 * Returns the meta object for class '{@link net.sf.rcpforms.emf.test.ConfigurationModel <em>Configuration Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Configuration Model</em>'.
	 * @see net.sf.rcpforms.emf.test.ConfigurationModel
	 * @generated
	 */
	EClass getConfigurationModel();

	/**
	 * Returns the meta object for the containment reference list '{@link net.sf.rcpforms.emf.test.ConfigurationModel#getTestModels <em>Test Models</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Test Models</em>'.
	 * @see net.sf.rcpforms.emf.test.ConfigurationModel#getTestModels()
	 * @see #getConfigurationModel()
	 * @generated
	 */
	EReference getConfigurationModel_TestModels();

	/**
	 * Returns the meta object for the containment reference '{@link net.sf.rcpforms.emf.test.ConfigurationModel#getTestModel <em>Test Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Test Model</em>'.
	 * @see net.sf.rcpforms.emf.test.ConfigurationModel#getTestModel()
	 * @see #getConfigurationModel()
	 * @generated
	 */
	EReference getConfigurationModel_TestModel();

	/**
	 * Returns the meta object for class '{@link net.sf.rcpforms.emf.test.AddressModel <em>Address Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Address Model</em>'.
	 * @see net.sf.rcpforms.emf.test.AddressModel
	 * @generated
	 */
	EClass getAddressModel();

	/**
	 * Returns the meta object for the attribute '{@link net.sf.rcpforms.emf.test.AddressModel#getZipCode <em>Zip Code</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Zip Code</em>'.
	 * @see net.sf.rcpforms.emf.test.AddressModel#getZipCode()
	 * @see #getAddressModel()
	 * @generated
	 */
	EAttribute getAddressModel_ZipCode();

	/**
	 * Returns the meta object for the attribute '{@link net.sf.rcpforms.emf.test.AddressModel#getValidFrom <em>Valid From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Valid From</em>'.
	 * @see net.sf.rcpforms.emf.test.AddressModel#getValidFrom()
	 * @see #getAddressModel()
	 * @generated
	 */
	EAttribute getAddressModel_ValidFrom();

	/**
	 * Returns the meta object for the attribute '{@link net.sf.rcpforms.emf.test.AddressModel#getValidTo <em>Valid To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Valid To</em>'.
	 * @see net.sf.rcpforms.emf.test.AddressModel#getValidTo()
	 * @see #getAddressModel()
	 * @generated
	 */
	EAttribute getAddressModel_ValidTo();

	/**
	 * Returns the meta object for the attribute '{@link net.sf.rcpforms.emf.test.AddressModel#isDifferentPostAddress <em>Different Post Address</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Different Post Address</em>'.
	 * @see net.sf.rcpforms.emf.test.AddressModel#isDifferentPostAddress()
	 * @see #getAddressModel()
	 * @generated
	 */
	EAttribute getAddressModel_DifferentPostAddress();

	/**
	 * Returns the meta object for the attribute '{@link net.sf.rcpforms.emf.test.AddressModel#getStreet <em>Street</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Street</em>'.
	 * @see net.sf.rcpforms.emf.test.AddressModel#getStreet()
	 * @see #getAddressModel()
	 * @generated
	 */
	EAttribute getAddressModel_Street();

	/**
	 * Returns the meta object for the attribute '{@link net.sf.rcpforms.emf.test.AddressModel#getHouseNumber <em>House Number</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>House Number</em>'.
	 * @see net.sf.rcpforms.emf.test.AddressModel#getHouseNumber()
	 * @see #getAddressModel()
	 * @generated
	 */
	EAttribute getAddressModel_HouseNumber();

	/**
	 * Returns the meta object for enum '{@link net.sf.rcpforms.emf.test.Gender <em>Gender</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Gender</em>'.
	 * @see net.sf.rcpforms.emf.test.Gender
	 * @generated
	 */
	EEnum getGender();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TestFactory getTestFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link net.sf.rcpforms.emf.test.impl.TestModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.rcpforms.emf.test.impl.TestModelImpl
		 * @see net.sf.rcpforms.emf.test.impl.TestPackageImpl#getTestModel()
		 * @generated
		 */
		EClass TEST_MODEL = eINSTANCE.getTestModel();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_MODEL__NAME = eINSTANCE.getTestModel_Name();

		/**
		 * The meta object literal for the '<em><b>Birth Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_MODEL__BIRTH_DATE = eINSTANCE.getTestModel_BirthDate();

		/**
		 * The meta object literal for the '<em><b>Age</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_MODEL__AGE = eINSTANCE.getTestModel_Age();

		/**
		 * The meta object literal for the '<em><b>Account Balance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_MODEL__ACCOUNT_BALANCE = eINSTANCE.getTestModel_AccountBalance();

		/**
		 * The meta object literal for the '<em><b>Is Selectable</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_MODEL__IS_SELECTABLE = eINSTANCE.getTestModel_IsSelectable();

		/**
		 * The meta object literal for the '<em><b>Address</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEST_MODEL__ADDRESS = eINSTANCE.getTestModel_Address();

		/**
		 * The meta object literal for the '<em><b>Overdraw Account</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_MODEL__OVERDRAW_ACCOUNT = eINSTANCE.getTestModel_OverdrawAccount();

		/**
		 * The meta object literal for the '<em><b>Child Count</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_MODEL__CHILD_COUNT = eINSTANCE.getTestModel_ChildCount();

		/**
		 * The meta object literal for the '<em><b>Gender</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_MODEL__GENDER = eINSTANCE.getTestModel_Gender();

		/**
		 * The meta object literal for the '{@link net.sf.rcpforms.emf.test.impl.ConfigurationModelImpl <em>Configuration Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.rcpforms.emf.test.impl.ConfigurationModelImpl
		 * @see net.sf.rcpforms.emf.test.impl.TestPackageImpl#getConfigurationModel()
		 * @generated
		 */
		EClass CONFIGURATION_MODEL = eINSTANCE.getConfigurationModel();

		/**
		 * The meta object literal for the '<em><b>Test Models</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONFIGURATION_MODEL__TEST_MODELS = eINSTANCE.getConfigurationModel_TestModels();

		/**
		 * The meta object literal for the '<em><b>Test Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONFIGURATION_MODEL__TEST_MODEL = eINSTANCE.getConfigurationModel_TestModel();

		/**
		 * The meta object literal for the '{@link net.sf.rcpforms.emf.test.impl.AddressModelImpl <em>Address Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.rcpforms.emf.test.impl.AddressModelImpl
		 * @see net.sf.rcpforms.emf.test.impl.TestPackageImpl#getAddressModel()
		 * @generated
		 */
		EClass ADDRESS_MODEL = eINSTANCE.getAddressModel();

		/**
		 * The meta object literal for the '<em><b>Zip Code</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ADDRESS_MODEL__ZIP_CODE = eINSTANCE.getAddressModel_ZipCode();

		/**
		 * The meta object literal for the '<em><b>Valid From</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ADDRESS_MODEL__VALID_FROM = eINSTANCE.getAddressModel_ValidFrom();

		/**
		 * The meta object literal for the '<em><b>Valid To</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ADDRESS_MODEL__VALID_TO = eINSTANCE.getAddressModel_ValidTo();

		/**
		 * The meta object literal for the '<em><b>Different Post Address</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ADDRESS_MODEL__DIFFERENT_POST_ADDRESS = eINSTANCE.getAddressModel_DifferentPostAddress();

		/**
		 * The meta object literal for the '<em><b>Street</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ADDRESS_MODEL__STREET = eINSTANCE.getAddressModel_Street();

		/**
		 * The meta object literal for the '<em><b>House Number</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ADDRESS_MODEL__HOUSE_NUMBER = eINSTANCE.getAddressModel_HouseNumber();

		/**
		 * The meta object literal for the '{@link net.sf.rcpforms.emf.test.Gender <em>Gender</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.rcpforms.emf.test.Gender
		 * @see net.sf.rcpforms.emf.test.impl.TestPackageImpl#getGender()
		 * @generated
		 */
		EEnum GENDER = eINSTANCE.getGender();

	}

} //TestPackage
