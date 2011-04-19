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
    post :create, {:email => 'p.muster@elmermx.ch', :password => '1234' } , :format => :json
    assert_response :success
  end
end
