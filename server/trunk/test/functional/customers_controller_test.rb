require 'test_helper'

class CustomersControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  setup do
    @customer = customers(:one)
    
    @request.env["devise.mapping"] = Devise.mappings[:user]
    @user = User.create! do |user|
      user.email = 'test@test.com'
      user.password = '12345'
      user.user_type = 1
    end
    sign_in @user
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:customers)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create customer" do
    assert_difference('Customer.count') do
      post :create, :customer => @customer.attributes
    end

    assert_redirected_to customer_path(assigns(:customer))
  end

  test "should show customer" do
    get :show, :id => @customer.to_param
    assert_response :success
  end

  test "should get edit" do
    get :edit, :id => @customer.to_param
    assert_response :success
  end

  test "should update customer" do
    put :update, :id => @customer.to_param, :customer => @customer.attributes
    assert_redirected_to customer_path(assigns(:customer))
  end

  test "should destroy customer" do
    assert_difference('Customer.count', -1) do
      delete :destroy, :id => @customer.to_param
    end

    assert_redirected_to customers_path
  end

  test "should get all customers when synchronizing without last_update" do
    post :synchronize, :format => :json
    assert_response :success
    assert_not_nil assigns(:updated_customers)
  end
end
