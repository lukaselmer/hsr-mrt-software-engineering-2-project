require 'test_helper'

class AddressesControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  setup do
    @address = addresses(:one)
    login_with_secretary
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:addresses)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create address" do
    assert_difference('Address.count') do
      post :create, :address => @address.attributes
    end

    assert_redirected_to address_path(assigns(:address))
  end

  test "should not create address without line1" do
    invalid_address = Address.new(:line2 => "Invalid Address")
    assert_no_difference('Address.count') do
      post :create, :address => invalid_address.attributes
    end
    assert_response :success
  end

  test "should show address" do
    get :show, :id => @address.to_param
    assert_response :success
  end

  test "should get edit" do
    get :edit, :id => @address.to_param
    assert_response :success
  end

  test "should update address" do
    put :update, :id => @address.to_param, :address => @address.attributes
    assert_redirected_to address_path(assigns(:address))
  end

  test "should not update address without line1" do
    @address.line1 = nil
    put :update, :id => @address.to_param, :address => @address.attributes
    assert_response :success
  end

  test "should destroy address" do
    assert_difference('Address.count', -1) do
      delete :destroy, :id => @address.to_param
    end

    assert_redirected_to addresses_path
  end
end
