require 'test_helper'

class SessionsControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  setup do
    # http://stackoverflow.com/questions/4291755/rspec-test-of-custom-devise-session-controller-fails-with-abstractcontrolleract
    setup_controller_for_warden
    request.env["devise.mapping"] = Devise.mappings[:user]
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "sould login by json" do
    post :create, :user => { :email => 'secretart@mrt.ch', :password => 'mrt' }, :format => :json
  end

  test "sould not login by json" do
    post :create, :user => { :email => 'secretary@mrt.ch', :password => 'wrong_passord' }, :format => :json
    assert_response 401
  end

  test "sould login by html" do
    post :create, :user => { :email => 'secretary@mrt.ch', :password => 'mrt' }
  end
  #test "sould not login by json with invalid credentials" do
  #  post :create, { :email => 'secretar@mrt.ch', :password => 'mrt' } , :format => :json
  #  assert_response :success
  #end


  #test "sould login" do
  #  post :create, :user => {
  #    :email => "nosuchuser@example.com",
  #    :password => "wevdude"
  #  }
  #  assert_response :success
  #end

  #test "sould not login invalid credentials" do
  #  post :create, { :email => 'secretar@mrt.ch', :password => 'mrt' }
  #  assert_response :success
  #end
end
