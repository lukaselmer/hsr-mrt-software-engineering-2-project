class ApplicationController < ActionController::Base
  protect_from_forgery
  before_filter :authenticate_user!

    def has_writeaccess?(user)

      acl = {
        CustomerController.class => [User::TYPES[:SECRETARY]]
      }

      acl_resource = acl[self.class]
      return acl_resource.include? user.user_type unless user.nil? or acl_resource.nil?
      false
    end
end
