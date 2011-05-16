require 'test_helper'

class UsersControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  setup do
    @user = users(:field_worker)
    login_with_secretary
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:users)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should not create user without email" do
    invalid_user = User.new( :password => '12345' )
    assert_no_difference('User.count') do
      post :create, :user => invalid_user.attributes
    end
    assert_response :success
  end

  test "should create user" do
    valid_user = { :email => 'test@test.com', :password => '12345' }
    assert_difference('User.count') do
      post :create, :user => valid_user
    end

    assert_redirected_to user_path(assigns(:user))
  end

  test "should show user" do
    get :show, :id => @user.to_param
    assert_response :success
  end

  test "should get edit" do
    get :edit, :id => @user.to_param
    assert_response :success
  end

  test "should not update user without email" do
    @user.email = nil
    put :update, :id => @user.to_param, :user => @user.attributes
    assert_response :success
  end

  test "should update user" do
    put :update, :id => @user.to_param, :user => @user.attributes
    assert_redirected_to user_path(assigns(:user))
  end

  test "should destroy user" do
    assert_difference('User.count', -1) do
      delete :destroy, :id => @user.to_param
    end

    assert_redirected_to users_path
  end
end