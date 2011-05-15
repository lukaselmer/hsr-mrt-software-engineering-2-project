require 'test_helper'

class OrdersControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  setup do
    @order = orders(:one)
    login_with_secretary
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:orders)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should not create order without customer_id" do
    invalid_order = Order.new()
    assert_no_difference('Order.count') do
      post :create, :order => invalid_order.attributes
    end
    assert_response :success
  end

  test "should create order" do
    assert_difference('Order.count') do
      post :create, :order => @order.attributes
    end

    assert_redirected_to order_path(assigns(:order))
  end

  test "should show order" do
    get :show, :id => @order.to_param
    assert_response :success
  end

  test "should get edit" do
    get :edit, :id => @order.to_param
    assert_response :success
  end

 test "should not update order without customer_id" do
    @order.customer = nil
    put :update, :id => @order.to_param, :order => @order.attributes
    assert_response :success
  end

  test "should update order" do
    put :update, :id => @order.to_param, :order => @order.attributes
    assert_redirected_to order_path(assigns(:order))
  end

  test "should destroy order" do
    assert_difference('Order.count', -1) do
      delete :destroy, :id => @order.to_param
    end

    assert_redirected_to orders_path
  end

  test "should add material to order" do
    get :add_material, :id => @order.to_param
    assert_not_nil assigns(:applied_material)
    assert_not_nil assigns(:order)
  end
end
