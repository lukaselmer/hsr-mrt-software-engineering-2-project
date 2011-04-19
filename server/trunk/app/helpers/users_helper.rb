module UsersHelper
  def can_modify?(user, resource)
    Rails.logger.debug resource.inspect

    acl = {
      UsersController.class => [User::TYPES[:SECRETARY], User::TYPES[:EMPLOYEE]]
    }

    acl_resource = acl[resource.class]


    return acl_resource.include? user.user_type unless user.nil? or acl_resource.nil?
    false
  end
end
