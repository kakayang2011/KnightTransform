package com.knight.transform.shrinkinline

import com.knight.transform.BaseContext
import com.knight.transform.MemberEntity
import com.knight.transform.Utils.TypeUtil
import com.knight.transform.shrinkinline.extension.ShrinkInlineExtension
import com.knight.transform.shrinkinline.weave.AccessMethodEntity
import org.gradle.api.Project
import java.util.concurrent.ConcurrentHashMap

class Context(project: Project,
              extension: ShrinkInlineExtension) : BaseContext(project, extension) {

    companion object {
        @JvmStatic
        fun getKey(owner: String, name: String, desc: String): String {
            return "$owner#$name#$desc"
        }
    }

    val accessMethods = ConcurrentHashMap<String, AccessMethodEntity>()
    val accessMemebers = ConcurrentHashMap<String, MemberEntity>()

    fun addAccessMethod(owner: String, name: String, desc: String): AccessMethodEntity {
        val entity = AccessMethodEntity(owner, name, desc)
        accessMethods.put(getKey(owner, name, desc), entity)
        return entity
    }


    @Synchronized
    fun addAccessedMemebers(owner: String, name: String, desc: String, isField: Boolean): MemberEntity {
        val targetKey = getKey(owner, name, desc)
        accessMemebers.get(targetKey)?.let {
            return it
        } ?: kotlin.run {
            val target = MemberEntity(-1, owner, name, desc, if (isField) MemberEntity.FIELD else MemberEntity.METHOD)
            accessMemebers.put(targetKey, target)
            return target
        }
    }

    fun isAccessMember(owner: String, name: String, desc: String): Boolean {
        return accessMemebers.containsKey(getKey(owner, name, desc))
    }

    fun isAccessMethod(owner: String, name: String, desc: String): Boolean {
        return accessMethods.containsKey(getKey(owner, name, desc))
    }

    fun isPrivateAccessMember(owner: String, name: String, desc: String): Boolean {
        val entity = accessMemebers.get(getKey(owner, name, desc))
        return entity != null && TypeUtil.isPrivate(entity.access)
    }

    fun getAccessMethod(owner: String, name: String, desc: String): AccessMethodEntity? {
        return accessMethods.get(getKey(owner, name, desc))
    }
}