ENV["RAILS_ENV"] = "test"
require File.expand_path('../../config/environment', __FILE__)
require 'rails/test_help'

class ActiveSupport::TestCase
  # Setup all fixtures in test/fixtures/*.(yml|csv) for all tests in alphabetical order.
  #
  # Note: You'll currently still have to declare fixtures explicitly in integration tests
  # -- they do not yet inherit this setting
  fixtures :all

  def login_with_secretary
    @request.env["devise.mapping"] = Devise.mappings[:user]
    secretary = users(:secretary)
    #    @user = User.create! do |user|
    #      user.email = 'test@test.com'
    #      user.password = '12345'
    #      user.user_type = User::TYPE_SECRETARY
    #    end
    sign_in secretary
  end

  def login_with_field_worker
    @request.env["devise.mapping"] = Devise.mappings[:user]
    worker = users(:field_worker)
    #    @user = User.create! do |user|
    #      user.email = 'test@test.com'
    #      user.password = '12345'
    #      user.user_type = User::TYPE_SECRETARY
    #    end
    sign_in worker
    return worker
  end

  # Add more helper methods to be used by all tests here...
end
